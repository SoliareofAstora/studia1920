select film.film_id, film.title, language.name
from film left join language on film.language_id=language.language_id;

select staff.staff_id, staff.first_name, staff.last_name, address.phone
from staff inner join address on staff.address_id = address.address_id;

select actor.first_name, actor.last_name
from actor inner join
    (select film_actor.actor_id from film_actor inner join
        (select film_id, title from film where title='Airport Pollock') f
    on film_actor.film_id = f.film_id)
        fa on actor.actor_id = fa.actor_id;

select customer.customer_id, customer.first_name, customer.last_name
from customer inner join (
    select rental.customer_id from rental group by rental.customer_id having count(rental.customer_id)>30
    ) r on customer.customer_id=r.customer_id;

select city.city, c.country from city left join country c on city.country_id = c.country_id;

select a.country from
    (select country.country from country
    inner join (select city.country_id from city group by country_id having count(city.city_id)>=10)
        c on country.country_id=c.country_id) a where country like 'T%';

select rental.rental_id from rental left join payment p on rental.rental_id = p.rental_id
where p.rental_id is null;

select r.rental_id, customer.first_name, customer.last_name from customer inner join
(select rental.rental_id, rental.customer_id from rental left join payment p on rental.rental_id = p.rental_id
where p.rental_id is null) r on r.customer_id = customer.customer_id;

select customer.first_name, customer.last_name, r.count from customer inner join
(select count(rental.rental_id), rental.customer_id from rental left join payment p on rental.rental_id = p.rental_id
where p.rental_id is null group by rental.customer_id) r on r.customer_id = customer.customer_id;

select language.name, count(f.language_id) from language inner join film f on language.language_id = f.language_id
group by language.name;

select customer_id, first_name, last_name, city from (select customer.customer_id, customer.first_name, customer.last_name, a.city_id from customer
    inner join address a on customer.address_id = a.address_id) b inner join city on city.city_id = b.city_id;

select film.film_id, film.title, film.description from film inner join
    (select film_category.film_id, c.name from film_category inner join category c on film_category.category_id = c.category_id
        where c.name in ('Comedy','Animation')) f on f.film_id = film.film_id;


