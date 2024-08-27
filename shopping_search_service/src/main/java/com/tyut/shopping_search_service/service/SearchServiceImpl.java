package com.tyut.shopping_search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.*;
import com.tyut.shopping_common.service.SearchService;
import com.tyut.shopping_search_service.repository.GoodsESRepository;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version v1.0
 * @author OldGj 2024/6/17
 * @apiNote 搜索服务实现类
 */
@DubboService
@Service
public class SearchServiceImpl implements SearchService {


    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private GoodsESRepository goodsESRepository;
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 分词方法
     * @param text 分词文本
     * @param analyzer 分词器
     */
    @SneakyThrows
    public List<String> analyze(String text, String analyzer) {
        // 创建分词请求
        AnalyzeRequest analyzeRequest = AnalyzeRequest.of(
                a -> a.index("goods")
                        .analyzer(analyzer)
                        .text(text));
        // 发送分词请求，获取相应结果
        AnalyzeResponse response = client.indices().analyze(analyzeRequest);
        // 处理相应结果
        List<String> words = new ArrayList<>(); // 分词结果集合
        List<AnalyzeToken> tokens = response.tokens();
        for (AnalyzeToken token : tokens) {
            String term = token.token();// 分出的词
            words.add(term);
        }
        return words;
    }

    /**
     * 自动补齐关键字
     * @param keyword 被补齐的词
     * @return 补齐的关键词集合
     */
    // 自动补齐
    @Override
    @SneakyThrows
    public List<String> autoSuggest(String keyword) {
        // 1.自动补齐查询条件
        Suggester suggester = Suggester.of(
                s -> s.suggesters("prefix_suggestion", FieldSuggester.of(
                        fs -> fs.completion(
                                cs -> cs.skipDuplicates(true) // 删除重复
                                        .size(10) // 10条
                                        .field("tags")
                        )
                )).text(keyword)
        );


        // 2.自动补齐查询
        SearchResponse<Map> response = client.search(
                s -> s.index("goods")
                        .suggest(suggester), Map.class);


        // 3.处理查询结果
        Map resultMap = response.suggest();
        List<Suggestion> suggestionList = (List) resultMap.get("prefix_suggestion");
        Suggestion suggestion = suggestionList.get(0);
        List<CompletionSuggestOption> resultList = suggestion.completion().options();


        List<String> result = new ArrayList<>();
        for (CompletionSuggestOption completionSuggestOption : resultList) {
            String text = completionSuggestOption.text();
            result.add(text);
        }
        return result;
    }

    /**
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return 搜索结果
     */
    @Override
    public GoodsSearchResult search(GoodsSearchParam goodsSearchParam) {
        // 1. 构造ES搜索条件
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);
        // 2. 搜索
        SearchHits<GoodsES> searchHits = template.search(nativeQuery, GoodsES.class);
        // 3.将搜索结果封装到mybatis-plus的page对象中
        Page<GoodsES> page = new Page<>();
        List<GoodsES> content = new ArrayList<>();
        for (SearchHit<GoodsES> searchHit : searchHits) {
            content.add(searchHit.getContent());
        }
        Page<GoodsES> goodsESPage = page.setCurrent(goodsSearchParam.getPage())// 当前页
                .setSize(goodsSearchParam.getSize()) // 每页条数
                .setTotal(searchHits.getTotalHits()) // 总条数
                .setRecords(content);// 当前页数据
        // 4. 封装搜索结果集对象
        GoodsSearchResult result = new GoodsSearchResult();
        // 4.1封装查询参数用于回显
        result.setGoodsSearchParam(goodsSearchParam);
        // 4.2封装当前页数据
        result.setGoodsPage(goodsESPage);
        // 4.3封装搜索面板
        buildSearchPanel(goodsSearchParam, result);
        return result;
    }

    /**
     * 封装查询面板
     * 根据搜索条件的前20条数据封装搜索面板中的品牌列表，类别列表，规格列表
     * @param goodsSearchParam 商品搜索条件
     * @param result 搜索结果
     */
    private void buildSearchPanel(GoodsSearchParam goodsSearchParam, GoodsSearchResult result) {
        // 构建搜索条件
        goodsSearchParam.setPage(1);
        goodsSearchParam.setSize(20);
        goodsSearchParam.setSort(null);
        goodsSearchParam.setSortFiled(null);
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);
        // 搜索
        SearchHits<GoodsES> searchHits = template.search(nativeQuery, GoodsES.class);
        List<GoodsES> content = new ArrayList<>();
        for (SearchHit<GoodsES> searchHit : searchHits) {
            content.add(searchHit.getContent());
        }
        // 遍历前二十条数据，获取品牌列表，类别列表，规格列表
        Set<String> brands = new HashSet<>();
        Set<String> productType = new HashSet<>();
        Map<String, Set<String>> specifications = new HashMap<>();
        for (GoodsES goodsES : content) {
            brands.add(goodsES.getBrand());
            productType.addAll(goodsES.getProductType());
            Map<String, List<String>> specification = goodsES.getSpecification();
            Set<Map.Entry<String, List<String>>> entries = specification.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                //  如果原来已经有规格，则新增规格项
                if (specifications.containsKey(key)) {
                    specification.get(key).addAll(value);
                } else { // 如果没有，则新增规格
                    Set<String> collect = value.stream()
                            .distinct()
                            .collect(Collectors.toSet());
                    specifications.put(key, collect);
                }
            }
        }
        result.setBrands(brands);
        result.setProductType(productType);
        result.setSpecifications(specifications);
    }

    /**
     * 构造搜索条件
     * @param goodsSearchParam 搜索条件
     */
    private NativeQuery buildQuery(GoodsSearchParam goodsSearchParam) {
        // 1.创建复杂查询条件对象
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        BoolQuery.Builder builder = new BoolQuery.Builder();
        // 2.如果关键字不为null，则在商品名，品牌名，副标题中根据关键字搜索，如果为null，则搜索全部
        String keyword = goodsSearchParam.getKeyword();
        if (!StringUtils.hasText(keyword)) { // 没有keyword
            // 搜索全部
            MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build();
            builder.must(matchAllQuery._toQuery());
        } else { // 存在keyword，在商品名，品牌名，副标题中根据关键字搜索
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                    q -> q.query(keyword)
                            .fields("goodsName", "caption", "brand"));
            builder.must(multiMatchQuery._toQuery());
        }
        // 3.根据品牌名精确搜索
        String brand = goodsSearchParam.getBrand();
        if (StringUtils.hasText(brand)) {
            TermQuery brandTermQuery = TermQuery.of(
                    q -> q.field("brand")
                            .value(brand));
            builder.must(brandTermQuery._toQuery());
        }
        // 4.如果存在最高价和最低价，则在最低价和最高价之间进行范围搜索
        Double highPrice = goodsSearchParam.getHighPrice();
        Double lowPrice = goodsSearchParam.getLowPrice();
        if (highPrice != null && highPrice != 0) {
            RangeQuery highPriceQuery = RangeQuery.of(
                    q -> q.field("price")
                            .lte(JsonData.of(highPrice)));
            builder.must(highPriceQuery._toQuery());
        }
        if (lowPrice != null && lowPrice != 0) {
            RangeQuery highPriceQuery = RangeQuery.of(
                    q -> q.field("price")
                            .gte(JsonData.of(lowPrice)));
            builder.must(highPriceQuery._toQuery());
        }
        // 5.如果查询条件有规格项，则精准匹配规格项
        Map<String, String> specificationOption = goodsSearchParam.getSpecificationOption();
        if (specificationOption != null && !specificationOption.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = specificationOption.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.hasText(key)) {
                    TermQuery specificationTermQuery = TermQuery.of(
                            q -> q.field("specification." + key + ".keyword").value(value));
                    builder.must(specificationTermQuery._toQuery());
                }
            }
        }
        // 将搜索条件加入到复杂查询对象nativeQueryBuilder中
        nativeQueryBuilder.withQuery(builder.build()._toQuery());

        // 6.添加分页条件
        PageRequest pageRequest = PageRequest.of(goodsSearchParam.getPage() - 1, goodsSearchParam.getSize());
        nativeQueryBuilder.withPageable(pageRequest);

        // 7.如果查询条件有排序，则添加排序条件
        // 排序字段
        String sortFiled = goodsSearchParam.getSortFiled();
        // 排序方式
        String sort = goodsSearchParam.getSort();
        if (StringUtils.hasText(sort) && StringUtils.hasText(sortFiled)) {
            Sort sortParam = null;
            // 新品的正序是id的倒序
            if (sortFiled.equals("NEW")) { // 按照新品排序
                if (sort.equals("ASC")) {
                    sortParam = Sort.by(Sort.Direction.DESC, "id");
                }
                if (sort.equals("DESC")) {
                    sortParam = Sort.by(Sort.Direction.ASC, "id");
                }
            }
            if (sortFiled.equals("PRICE")) { // 按照价格排序
                if (sort.equals("ASC")) {
                    sortParam = Sort.by(Sort.Direction.ASC, "price");
                }
                if (sort.equals("DESC")) {
                    sortParam = Sort.by(Sort.Direction.DESC, "price");
                }
            }
            if (sortParam != null) {
                nativeQueryBuilder.withSort(sortParam);
            }
        }
        // 8.返回查询条件对象
        return nativeQueryBuilder.build();
    }

    /**
     * 向ES同步数据库中的商品数据
     * @param goodsDesc 商品详情
     */
    @Override
    public void syncGoodsToES(GoodsDesc goodsDesc) {
        // 将GoodsDesc转为GoodsES对象
        GoodsES goodsES = new GoodsES();
        goodsES.setId(goodsDesc.getId());
        String goodsName = goodsDesc.getGoodsName();
        goodsES.setGoodsName(goodsName);
        goodsES.setCaption(goodsDesc.getCaption());
        goodsES.setPrice(goodsDesc.getPrice());
        goodsES.setHeaderPic(goodsDesc.getHeaderPic());
        goodsES.setBrand(goodsDesc.getBrand().getName());
        ProductType productType1 = goodsDesc.getProductType1();
        ProductType productType2 = goodsDesc.getProductType2();
        ProductType productType3 = goodsDesc.getProductType3();
        // 设置
        List<ProductType> productTypeList = new ArrayList<>();
        productTypeList.add(productType1);
        productTypeList.add(productType2);
        productTypeList.add(productType3);
        goodsES.setProductType(productTypeList
                .stream()
                .map(ProductType::getName)
                .collect(Collectors.toList()));
        List<String> tags = new ArrayList<>();
        List<String> goodsNameKeyword = analyze(goodsName, "ik_smart");
        List<String> goodsCaptionKeyword = analyze(goodsDesc.getCaption(), "ik_smart");
        tags.add(goodsDesc.getBrand().getName()); // 商品品牌是关键词
        tags.addAll(goodsNameKeyword);// 商品名称分词后做关键词
        tags.addAll(goodsCaptionKeyword); // 商品副标题分词后做关键词
        goodsES.setTags(tags);
        Map<String, List<String>> specifications = new HashMap<>(); // 【key，value】 =》 【规格名称，规格项集合】
        // 商品规格
        List<Specification> specificationList = goodsDesc.getSpecifications();
        for (Specification specification : specificationList) {
            String specName = specification.getSpecName(); // key
            List<SpecificationOption> optionList = specification.getSpecificationOptions(); // 规格项
            // 规格项名称
            List<String> specStrList = optionList.stream()
                    .map(SpecificationOption::getOptionName)
                    .toList();
            specifications.put(specName, specStrList);
        }
        goodsES.setSpecification(specifications);
        // 将GoodsES对象保存到es中
        goodsESRepository.save(goodsES);
    }

    /**
     * 删除ES中的商品数据
     * @param id 商品id
     */
    @Override
    public void delete(Long id) {
        goodsESRepository.deleteById(id);
    }
}
