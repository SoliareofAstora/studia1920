/*Wyświetl z tablei Staff wszystkie rekordy dla których picture jest NULL*/
select * from staff where picture is null;

/*Id i nazwę  kategorii, dla kategorii które mają w nazwie drugą literę „o”*/
select category_id, name from category where name like '_o%';

/*Id, i mię i nazwisko klientów których imiona nie zaczynają się od litery M, D a nazwiska nie kończą się na E*/
select customer_id, first_name, last_name from customer
where first_name not like 'M%' and first_name not like 'D%' and last_name not like '%e'
order by first_name;

/*Wszystkie dane o płatnościach dla których customer_id to 147, 114 i 128 a wartość płatności jest różna od 2.99*/
select * from payment where customer_id in (147, 114, 128) and amount != 2.99;

/*Wszystkie dane o płatnościach dla których customer_id nie jest w przedziale 200 –300*/
select * from payment where customer_id not between 200 and 300;

/* Wszystkie dane o płatnościach dla których wartość płatności to 2.99, 4.99, 5,99 lub zawiera się w przedziale 0.1 –0.99, a data płatności starsza/większa niż 01.04.2007. Posortuj po wartości płatności malejąco*/
select * from payment
where (amount in (2.99, 4.99, 5.99) or amount between 0.1 and 0.99)
and payment_date > '2007-04-15';








