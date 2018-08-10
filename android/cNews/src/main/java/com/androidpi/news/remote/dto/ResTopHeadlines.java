package com.androidpi.news.remote.dto;

import com.androidpi.news.entity.News;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/9.
 */
public class ResTopHeadlines {


    /**
     * status : ok
     * totalResults : 20
     * articles : [{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"卫健委通报陕西疫苗事件：疫苗未过期系登记错误","description":"调查还发现3个接种点存在接种门诊硬件设施不达标、接种工作人员数量不足、预防接种管理信息化程度低、监督检查不到位等问题。","url":"http://www.chinanews.com/gn/2018/08-09/8595157.shtml","urlToImage":null,"publishedAt":"2018-08-09T13:45:19Z"},{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"西藏军区边防某团开展工程防化战术综合演练","description":"(李德成 谷杰 赵朗)为提升协同配属及协同作战能力，近日，西藏军区边防某团在海拔4500米地域组织所属工程防化分队进行战术综合演练。","url":"http://www.chinanews.com/mil/2018/08-09/8595137.shtml","urlToImage":null,"publishedAt":"2018-08-09T13:22:09Z"},{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"英仙座流星雨下周一凌晨最盛中国处理想观测位置","description":"(郭军 李建基)据广州市五羊天象馆透露：今年英仙座流星雨极盛期出现在8月13日凌晨4时前后，我国各地最佳观测时间是8月13日凌晨0时至天亮前。","url":"http://www.chinanews.com/sh/2018/08-09/8595086.shtml","urlToImage":null,"publishedAt":"2018-08-09T12:21:10Z"},{"source":{"id":null,"name":"Sina.com.cn"},"author":"","title":"新华社：贸易霸凌主义来势汹汹没有磨砺就没成长","description":"新华社：贸易霸凌主义来势汹汹 没有磨砺就没成长","url":"http://news.sina.com.cn/c/zj/2018-08-09/doc-ihhnunsq2395543.shtml","urlToImage":"http://n.sinaimg.cn/front/406/w772h434/20180809/ocMT-hhnunsq2395799.jpg","publishedAt":"2018-08-09T12:06:02Z"},{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"7个月260次中国楼市进入\u201c调控年\u201d","description":"(张文绞)在\u201c坚决遏制房价上涨\u201d的政策目标下，中国楼市调控鼓点密集。","url":"http://www.chinanews.com/cj/2018/08-09/8595061.shtml","urlToImage":null,"publishedAt":"2018-08-09T11:20:24Z"},{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"河南滑县回应多部门经商办企：纪委监委介入调查","description":"媒体报道称，滑县文森开发管理有限责任公司是一家注册资本1000万元，成立于2016年3月的企业，法定代表人刘传丰，目前是国有滑县林场的负责人。","url":"http://www.chinanews.com/gn/2018/08-09/8595041.shtml","urlToImage":null,"publishedAt":"2018-08-09T11:07:01Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"划重点！一文掌握习近平外交思想的理论要义","description":"划重点！一文掌握习近平外交思想的理论要义-新华网","url":"http://www.xinhuanet.com/politics/2018-08/09/c_1123247167.htm","urlToImage":null,"publishedAt":"2018-08-09T10:05:45Z"},{"source":{"id":null,"name":"Huanqiu.com"},"author":null,"title":"社评：美对华贸易战是霸权主义在全球化时代的挣扎","description":null,"url":"http://opinion.huanqiu.com/editorial/2018-08/12683151.html","urlToImage":null,"publishedAt":"2018-08-09T09:42:15Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"习近平眼中\u201c大党的样子\u201d","description":"习近平眼中\u201c大党的样子\u201d\r\n---习近平总书记在十九届中共中央政治局常委同中外记者见面以及在中国共产党与世界政党高层对话会上，都提到中国共产党是世界上最大的政党，\u201c大就要有大的样子\u201d的重大命题。","url":"http://www.xinhuanet.com/politics/2018-08/09/c_1123246276.htm","urlToImage":null,"publishedAt":"2018-08-09T09:41:24Z"},{"source":{"id":null,"name":"Sina.com.cn"},"author":"第一财经","title":"日美贸易磋商开启:日本寻求汽车税豁免、美国要谈FTA","description":"日美贸易磋商开启:日本寻求汽车税豁免、美国要谈FTA","url":"http://finance.sina.com.cn/world/2018-08-09/doc-ihhnunsq1253307.shtml","urlToImage":"//n.sinaimg.cn/finance/transform/96/w542h354/20180809/i4Sy-hhnunsq1250317.png","publishedAt":"2018-08-09T08:52:00Z"},{"source":{"id":null,"name":"Eastmoney.com"},"author":null,"title":"最高法出台意见为海南全面深化改革开放提供司法保障","description":"记者9日从最高人民法院获悉，最高法近日出台关于为海南全面深化改革开放提供司法服务和保障的意见，充分发挥人民法院职能作用，推动海南自由贸易试验区和中国特色自由贸易港建设。意见强调，要充分发挥司法职能，加强审判执行工作，推动海南构建法治化、国际化、便利化的营商环境和公平开放统一的市场环境。加强刑事审判，严厉打击影响海南全面深化改革开放的各类刑事犯罪。","url":"http://finance.eastmoney.com/news/1350,20180809923571786.html","urlToImage":null,"publishedAt":"2018-08-09T08:51:41Z"},{"source":{"id":null,"name":"People.com.cn"},"author":"3370","title":"双胞胎溺亡后仍有人犯险是何心态？","description":"双胞胎溺亡后仍有人犯险是何心态？背景：近日，一对到青岛旅游的北京8岁双胞胎姐妹在黄岛区某海滩失踪双双溺亡。目前在事发海滩，每隔数十米就有禁止游泳的提示牌，还有广播播放\u201c水下有暗流，禁止下海游泳，注意人","url":"http://opinion.people.com.cn/n1/2018/0809/c119388-30219907.html","urlToImage":null,"publishedAt":"2018-08-09T08:23:38Z"},{"source":{"id":null,"name":"Finance.ce.cn"},"author":"马欣","title":"海南股权交易中心\u201c科创板\u201d启动","description":null,"url":"http://finance.ce.cn/stock/gsgdbd/201808/09/t20180809_29990230.shtml","urlToImage":null,"publishedAt":"2018-08-09T07:49:00Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"放学后，你该把孩子交给谁？杭州将全面实行免费晚托班","description":"放学后，你该把孩子交给谁？杭州将全面实行免费晚托班\r\n---为了解决家长们的后顾之忧，杭州市教育局近日出台了《关于推行小学生放学后托管服务工作的指导意见（试行）》，从今年9月起，杭州的小学将全面实行免费晚托班，迈出了政府兜底构建托管机制的重要一步。","url":"http://www.xinhuanet.com/politics/2018-08/09/c_1123246786.htm","urlToImage":null,"publishedAt":"2018-08-09T07:30:55Z"},{"source":{"id":null,"name":"Chinanews.com"},"author":"chinanews","title":"\u201c杀鱼弟\u201d病情趋于稳定处于急性肾衰竭尿毒症状态","description":"记者当天在医院了解到，目前孟凡森病情趋于稳定，进一步恶化的势头被遏制，但仍处于急性肾衰竭的尿毒症状态。","url":"http://www.chinanews.com/sh/2018/08-09/8594666.shtml","urlToImage":null,"publishedAt":"2018-08-09T06:12:59Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"江西实施鄱阳湖生态环境专项整治力保一湖清水入长江","description":"江西实施鄱阳湖生态环境专项整治 力保一湖清水入长江\r\n---针对中央环保督察\u201c回头看\u201d反映的问题，统筹考虑鄱阳湖与入湖河流的关系，江西省近日出台文件，坚持河湖同治、水陆共治，实施鄱阳湖生态环境专项整治，力保一湖清水入长江。","url":"http://www.xinhuanet.com/politics/2018-08/09/c_1123246179.htm","urlToImage":null,"publishedAt":"2018-08-09T04:25:14Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"北京地铁1号线与八通线贯通工程进入审批阶段","description":"八通线南延后将缩短发车间隔。数据分析显示，八通线的客流中，83%的乘客在地铁四惠站、四惠东站换乘，为两座换乘站带来巨大压力。芍药居站是10号线和13号线换乘站，目前两站依靠一条70多米长的换乘通道连通，几乎无法满足大客流换乘需求。","url":"http://www.bj.xinhuanet.com/tt/2018-08/09/c_1123244171.htm","urlToImage":null,"publishedAt":"2018-08-09T03:27:01Z"},{"source":{"id":null,"name":"Huanqiu.com"},"author":null,"title":"\u201c空中警察\u201d：眼神犀利的警用无人机","description":null,"url":"http://uav.huanqiu.com/hyg/2018-08/12679011.html","urlToImage":null,"publishedAt":"2018-08-09T00:40:17Z"},{"source":{"id":"xinhua-net","name":"Xinhua Net"},"author":null,"title":"北京市昨日降雨现七年来最大雨强","description":"北京市昨日降雨现七年来最大雨强\r\n---8月7日21时30分，我国正式进入立秋节气。立秋当日，北京也迎来新一轮降雨。","url":"http://www.xinhuanet.com/local/2018-08/09/c_1123243596.htm","urlToImage":null,"publishedAt":"2018-08-09T00:01:56Z"},{"source":{"id":null,"name":"Feiyi.gmw.cn"},"author":null,"title":"中国非物质文化遗产展在世界最古老的图书馆精彩亮相","description":null,"url":"http://feiyi.gmw.cn/2018-08/08/content_30387550.htm","urlToImage":null,"publishedAt":"2018-08-08T01:16:03Z"}]
     */

    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("articles")
    private List<ArticlesBean> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {
        /**
         * source : {"id":null,"name":"Chinanews.com"}
         * author : chinanews
         * title : 卫健委通报陕西疫苗事件：疫苗未过期系登记错误
         * description : 调查还发现3个接种点存在接种门诊硬件设施不达标、接种工作人员数量不足、预防接种管理信息化程度低、监督检查不到位等问题。
         * url : http://www.chinanews.com/gn/2018/08-09/8595157.shtml
         * urlToImage : null
         * publishedAt : 2018-08-09T13:45:19Z
         */

        @SerializedName("source")
        private SourceBean source;
        @SerializedName("author")
        private String author;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("url")
        private String url;
        @SerializedName("urlToImage")
        private String urlToImage;
        @SerializedName("publishedAt")
        private String publishedAt;

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public News toNewsArticle() {
            News news = new News();
            news.setAuthor(author);
            news.setDescription(description);
            news.setPublishedAt(publishedAt);
            news.setSource(source.toSource());
            news.setTitle(title);
            news.setUrl(url);
            news.setUrlToImage(urlToImage);
            return news;
        }

        public static class SourceBean {
            /**
             * id : null
             * name : Chinanews.com
             */

            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public News.Source toSource() {
                News.Source source = new News.Source();
                source.setId(id);
                source.setName(name);
                return source;
            }
        }
    }
}
