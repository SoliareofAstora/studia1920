--1

SELECT products.product_id, products.product_name
FROM products
WHERE products.product_id IN (SELECT product_id
							FROM order_details
							WHERE quantity > 12);


SELECT DISTINCT products.product_id, products.product_name
FROM products
JOIN order_details
ON products.product_id = order_details.product_id
WHERE order_details.quantity > 12
ORDER BY product_id;

--2
SELECT products.product_id, products.product_name
FROM products
WHERE products.supplier_id IN (SELECT suppliers.supplier_id
							  FROM suppliers
							  WHERE suppliers.country LIKE 'USA' OR suppliers.country LIKE 'UK');


SELECT products.product_id, products.product_name, suppliers.country
FROM products
JOIN suppliers
ON products.supplier_id = suppliers.supplier_id
WHERE suppliers.country LIKE 'USA' OR suppliers.country LIKE 'UK';


--3
select * from products where unit_price < (select avg(unit_price) from products where discontinued!=0);
--4
select company_name from suppliers where suppliers.supplier_id in (select supplier_id from products where unit_price>40);

select company_name from suppliers where exists
    (select supplier_id from products where (unit_price>40
                                        and products.supplier_id= suppliers.supplier_id));

select distinct company_name from suppliers inner join (select * from products where unit_price>40) p on suppliers.supplier_id = p.supplier_id;

--5
select product_id, product_name, unit_price from products
where product_id in (select product_id from products order by unit_price desc limit 10);

--6
select distinct company_name
from suppliers inner join (
    select *
    from products
    where product_id in (
        select product_id from order_details
        where (discount=0 and order_id in (
            select order_id
            from orders
            where ship_city in ('Paris', 'Salzburg'))
        )
    )
) p on suppliers.supplier_id = p.supplier_id;









