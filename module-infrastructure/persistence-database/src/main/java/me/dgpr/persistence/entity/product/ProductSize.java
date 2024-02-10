package me.dgpr.persistence.entity.product;

public enum ProductSize {
    SMALL("SMALL"),
    LARGE("LARGE");

    private final String size;

    ProductSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public static ProductSize from(String size) {
        for (ProductSize ps : ProductSize.values()) {
            if (ps.getSize().equalsIgnoreCase(size)) {
                return ps;
            }
        }
        return null;
    }
}
