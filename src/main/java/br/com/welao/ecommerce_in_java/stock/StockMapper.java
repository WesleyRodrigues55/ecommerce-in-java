package br.com.welao.ecommerce_in_java.stock;

public class StockMapper {
    public static Stock toEntity(StockDTO dto) {
        Stock stock = new Stock();

        stock.setName(dto.getName());
        stock.setQuantity(dto.getQuantity());
        stock.setPrice(dto.getPrice());
        stock.setActive(dto.getActive());
        stock.setProducts(dto.getProducts());

        return stock;
    }

    public static StockDTO toDto(Stock stock) {
        StockDTO dto = new StockDTO();

        dto.setName(stock.getName());
        dto.setQuantity(stock.getQuantity());
        dto.setPrice(stock.getPrice());
        dto.setActive(stock.getActive());
        dto.setProducts(stock.getProducts());

        return dto;
    }
}
