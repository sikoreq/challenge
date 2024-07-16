CREATE TABLE public.city (
                             id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
                             "name" varchar(255) NOT NULL,
                             CONSTRAINT city_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX city_name_idx ON public.city ("name");

CREATE TABLE public.temperature_average (
                                            id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
                                            sum numeric NOT NULL,
                                            number_of_measurements int8 NOT NULL,
                                            city_id int4 NOT NULL,
                                            CONSTRAINT temperature_average_pk PRIMARY KEY (id),
                                            CONSTRAINT temperature_average_fk FOREIGN KEY (city_id) REFERENCES public.city(id)
);
CREATE INDEX temperature_average_city_id_idx ON public.temperature_average (city_id);

CREATE TABLE public.measurement (
                                    id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
                                    city_id int4 NOT NULL,
                                    "date" timestamp NOT NULL,
                                    CONSTRAINT measurement_pk PRIMARY KEY (id)
);
CREATE INDEX measurement_city_id_idx ON public.measurement (city_id);
CREATE INDEX measurement_date_idx ON public.measurement ("date");
CREATE INDEX measurement_city_id_date_idx ON public.measurement (city_id,"date");

