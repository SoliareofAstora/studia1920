--1
--2
--3
--4

select
    order_details.product_id as id,
    max(unit_price) as max_price,
    p.name as name
from order_details
inner join (
    select product_id, product_name as name from products
    ) as p on order_details.product_id = p.product_id
group by order_details.product_id, p.name, order_details.product_id


--5
--6
--7
