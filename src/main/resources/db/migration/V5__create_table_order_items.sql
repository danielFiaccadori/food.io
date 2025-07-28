CREATE TABLE order_items (
    -- Embedded id (order_id and product_id)
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    quantity INTEGER NOT NULL,

    CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT product_fk FOREIGN KEY (product_id) REFERENCES products(id),

    PRIMARY KEY (order_id, product_id)
);