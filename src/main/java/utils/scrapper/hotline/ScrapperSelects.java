package utils.scrapper.hotline;

public interface ScrapperSelects {

    static String SELECTOR_NAME(int i) {
        return "#page-sales > div.cell-fixed-indent.cell-md > div > div.viewbox > div.clearfix > div > ul > li:nth-child(" + i + ") > div.row.text-center > div.cell-7 > div.m_b-md > span";
    }

    static String SELECTOR_PRICE(int i) {
        return "#page-sales > div.cell-fixed-indent.cell-md > div > div.viewbox > div.clearfix > div > ul > li:nth-child(" + i + ") > div.item-price.stick-bottom > div:nth-child(1) > span.price-format";
    }

    static String SELECTOR_URL_GOOD(int i) {
        return "#page-sales > div.cell-fixed-indent.cell-md > div > div.viewbox > div.clearfix > div > ul > li:nth-child(" + i + ") > div.item-info-top > a";
    }
}
