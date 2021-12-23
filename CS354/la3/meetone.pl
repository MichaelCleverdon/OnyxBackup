#!/bin/gprolog --consult-file

:- include('data.pl').

lte(time(PersonHour, PersonMinute, PersonMeridian), time(Hour,Minute,Meridian)) :- (PersonMeridian = am, Meridian = pm); (PersonMeridian=Meridian, (PersonHour<Hour; (PersonHour=Hour, PersonMinute=<Minute))).

gte(time(PersonHour,PersonMinute,PersonMeridian), time(Hour,Minute,Meridian)) :- (PersonMeridian = pm, Meridian = am); (PersonMeridian = Meridian, (PersonHour > Hour; (PersonHour = Hour, PersonMinute >= Minute))).

meetone(Person, slot(Time1, Time2)) :- free(Person,slot(PersonTime1,PersonTime2)), lte(PersonTime1, Time1), gte(PersonTime2, Time2).

main :- findall(Person,
		meetone(Person,slot(time(8,30,am),time(8,45,am))),
		People),
	write(People), nl, halt.

:- initialization(main).