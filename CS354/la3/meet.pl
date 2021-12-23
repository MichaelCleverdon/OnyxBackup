#!/bin/gprolog --consult-file

:- include('data.pl').
:- include('uniq.pl').

lt(time(PersonHour, PersonMinute, PersonMeridian), time(Hour,Minute,Meridian)) :- (PersonMeridian = am, Meridian = pm); (PersonMeridian=Meridian, (PersonHour<Hour; (PersonHour=Hour, PersonMinute=<Minute))).

gt(time(PersonHour,PersonMinute,PersonMeridian), time(Hour,Minute,Meridian)) :- (PersonMeridian = pm, Meridian = am); (PersonMeridian = Meridian, (PersonHour > Hour; (PersonHour = Hour, PersonMinute >= Minute))).

checkNotEqual(time(Hour1, Minute1, Meridian1), time(Hour2, Minute2, Meridian2)) :- (Meridian1 \= Meridian2); (Meridian1=Meridian2, Hour1\=Hour2);(Meridian1=Meridian2,Hour1=Hour2, Minute1\=Minute2).
%Finds the least out of 3 Times
least(Time1, Time2, Time3, NewTime) :- (lt(Time1, Time2), lt(Time1, Time3), makeNewTime(Time1, NewTime));
                                       (lt(Time2, Time1), lt(Time2, Time3), makeNewTime(Time2, NewTime));
                                       (lt(Time3, Time1), lt(Time3, Time2), makeNewTime(Time3, NewTime)).

greatest(Time1, Time2, Time3, NewTime) :- (gt(Time1, Time2), gt(Time1, Time3), makeNewTime(Time1, NewTime));
                                            (gt(Time2, Time1), gt(Time2, Time3), makeNewTime(Time2, NewTime));
                                            (gt(Time3, Time1), gt(Time3, Time2), makeNewTime(Time3, NewTime)).

makeNewTime(time(Hour, Minute, Meridian), time(Hour,Minute,Meridian)).
makeNewSlot(Time1, Time2, slot(Time1, Time2)).

overlap(P1Time1, P1Time2, P2Time1, P2Time2, P3Time1, P3Time2, StartTime, EndTime) :- greatest(P1Time1, P2Time1, P3Time1, StartTime),
    least(P1Time2, P2Time2, P3Time2, EndTime), lt(StartTime, EndTime), checkNotEqual(StartTime, EndTime).
    % (lte(P1Time1, P2Time1), lte(P1Time2, P2Time2), makeNewSlot(P2Time1, P1Time2, Slot));
    % (gte(P1Time1, P2Time1), gte(P1Time2, P2Time2), makeNewSlot(P1Time1, P2Time2));
    % (gte(P1Time1, P2Time1), lte(P1Time2, P2Time2), makeNewSlot(P1Time1, P1Time2)).

peopleAndTime(StartTime, EndTime) :- people([X,Y,Z]),
    free(X,slot(P1Time1, P1Time2)), free(Y, slot(P2Time1, P2Time2)), free(Z, slot(P3Time1, P3Time2)),
    overlap(P1Time1, P1Time2, P2Time1, P2Time2, P3Time1, P3Time2, StartTime, EndTime).
    % overlap(P2Time1, P2Time2, P3Time1, P3Time2, Slot),             write('P1 Times '), write(P1Time1), nl, write('P2 Times '), write(P2Time1), nl, write('P3 Times '), write(P3Time1), nl,
    % overlap(P1Time1, P1Time2, P3Time1, P3Time2, Slot).

meet(slot(StartTime, EndTime)) :- peopleAndTime(StartTime, EndTime).

%fact of people
people([ann,bob,carla]).

main :- findall(Slot, meet(Slot), Slots),
        uniq(Slots, Uniq),
        write(Uniq), nl, halt.

:- initialization(main).
