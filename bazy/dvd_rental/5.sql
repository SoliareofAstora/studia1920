/*
 1.Najmniejszą wartość płatności z tabeli payment
 2.Najdłuższy film
 3.Liczbę wszystkich miast z tabeli city
 4.Średnią długość filmuz tabeli film
 5.Sumępłatności z tabeli payment
 6.Sumę wszystkich płatności dla danego staff_id
 7.Liczbę płatności po 6 kwietnia 2012
 8.Liczbę klientów których nazwiska zaczynają się na literę B i D
 9.Liczbę adresów dla danego rejonu (district)
 10.Liczbę adresów dla danego rejonu (district), wypisz tylko przypadki dla których liczba jest większa od 2, posortuj po district
 11.Liczbę wypożyczonych filmów (tabela rental) zwróconych po 01.06.2005
 12.Średnią długość wypożyczenia filmów(rental_duration)w zależności odwartości rental_rate
 13.Średnią długość wypożyczenia filmów (rental_duration) dla których rental rate = 0.99lub 4.99, w zależności od wartości replacement_cost. Wyświetl tylko filmy których średnia długość wypożyczenia jest mniejsza od 5,a replacement_cost mniejszy od20.99 , posortuj rosnąco po średniej długości wypożyczenia
 14.Sumę płatności dla danego kleinta (client_id) i danegostaff_id, uwzględnij tylko płatności dla których suma płatościmieści się w przedziale 50 –60ale różną od 50.87, posortuj po client_id
 */

select min(amount) from payment;

select max(film.length) from film;
select * from film where length= (select max(length) from film);
select length from film group by length having length = max(length);

select count(city.city_id) from city;

select avg(length) from film;

select sum(amount) from payment;

select sum(amount), staff_id from payment group by staff_id;

select count(amount) from payment where payment_date > '2012-04-06';

select substr(last_name,1,1), count(first_name)
  from customer  where last_name like 'B%' or last_name like 'D%'
 group by substr(last_name,1,1);

select count(address), district from address group by district;

select count(address), district from address
group by district having count(address)>2 order by district;

select count(rental.rental_id) from rental where return_date>'2005-06-01';

select rental_rate, avg(film.rental_duration) from film group by rental_rate;

select replacement_cost, avg(rental_duration) from film
where rental_rate in (0.99, 4.99) and rental_duration<5
group by replacement_cost
having replacement_cost<20.99
and avg(rental_duration)<5;

select customer_id, staff_id, sum(amount)
from payment group by customer_id, staff_id
having sum(amount) between 50 and 60 and sum(amount)!=50.87
order by customer_id;