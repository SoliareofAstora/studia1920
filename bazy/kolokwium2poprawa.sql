-- 1. Programy posiadają liczbę sezonów (tabela episode_list) lub nie. Podaj liczbę programów
-- emitowanych w danej liczbie sezonów.
-- Tabela wynikowa: liczba sezonów, liczba_programów

select total_seasons, count(content_id)
from
     contents
group by total_seasons order by total_seasons;

select total_seasons, count(content_id)
from
    (
        select content_id, max(season_num) as total_seasons
        from
             episode_list
        group by content_id
    ) as a
group by total_seasons;

-- 2. Odpowiedz na pytania:
-- a) Którego gatunku (tabela genres) programów jest najwięcej?

select g.name, count(c.content_id) amount
from
    content_genres c,
    genres g
where c.genre_id=g.genre_id
group by g.name order by amount desc limit 1;


-- b) Jaka jest liczba programów , których liczba epizodów (total_episodes) jest większa od 100?

select count(total_episodes)
from
     contents
where total_episodes>100;

-- 3. Podaj nazwiska aktorów, którzy grali w programach z gatunku komedia (tabela genres). Tabela
-- wynikowa: nazwiska

select regexp_replace(a.name,'^.* ', '')
from
    actors a,
    (
        select distinct actor_id
        from
             content_actors ca,
             (
                 select content_id from content_genres where content_genres.genre_id=
                    (select genre_id from genres where name like 'Comedy')
             ) c
        where ca.content_id=c.content_id
    ) aid
where a.actor_id=aid.actor_id;

-- 4. Czy w bazie danych występują osoby (imię nazwisko) które w tym samym programie miały dwie
-- funkcje tj aktora i dyrektora? Podaj nazwiska i tytuł programu.

select regexp_replace(s.name,'^.* ', ''), title
from
    contents,
    (
        select a.content_id , a.name
        from
            (
                select content_id, actors.name
                from
                     content_actors,
                     actors
                where content_actors.actor_id=actors.actor_id
            ) as a,
            (
                select content_id, directors.name
                from
                     content_directors,
                     directors
                where content_directors.director_id=directors.director_id
            ) as d
        where a.content_id=d.content_id and a.name=d.name
    ) as s
where s.content_id = contents.content_id;

-- 5. Wyświetl tytuły programów, dla których wszystkie epizody zostały ocenione wyżej od średniej z
-- wszystkich ocen (episode_rating z tabeli episode_list). Wykorzystaj podzapytanie. Wynik: tytuły
-- programów

select c.title
from
    (
        select total.content_id
        from
            (
                select content_id, count(episode_id) total
                from
                     episode_list
                group by content_id
            ) as total,
            (
                select content_id, count(episode_id) super
                from
                     episode_list
                where episode_rating>(select avg(episode_rating) from episode_list)
                group by content_id
            ) as adbove
        where total.content_id=adbove.content_id and total.total=adbove.super
    ) as good_stuff,
    contents as c
where c.content_id=good_stuff.content_id;