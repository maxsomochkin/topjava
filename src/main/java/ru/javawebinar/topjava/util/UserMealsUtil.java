package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = /*filteredByCycles*/filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 300);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        List<UserMeal> mealFilterDates = new ArrayList<>();


        for (UserMeal meal: meals)
            if(startTime.isBefore(meal.getDateTime().toLocalTime()) && endTime.isAfter(meal.getDateTime().toLocalTime()))
               mealFilterDates.add(meal);

        Map<LocalDate, Integer> dayMealMap = new HashMap<>();

        mealFilterDates.forEach(dayMeal ->
                dayMealMap.merge(dayMeal.getDateTime().toLocalDate(),dayMeal.getCalories(),
                        Integer::sum));

        dayMealMap.values().removeIf(calories -> calories<caloriesPerDay);

        mealFilterDates.forEach(userMeal -> {
            if (dayMealMap.containsKey(userMeal.getDateTime().toLocalDate()))
                mealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),true));
        });


        return mealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<Object, Integer> collect = meals
                .stream()
                .filter(userMeal -> startTime.isBefore(userMeal.getDateTime().toLocalTime())
                        && endTime.isAfter(userMeal.getDateTime().toLocalTime()))
                .collect(Collectors.groupingBy(mealDate -> mealDate.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        collect.values().forEach(System.out::println);
        return null;
//               .entrySet()
//               .stream()
//               .filter(mealDate->mealDate.getValue()>caloriesPerDay)
//               .collect(new ArrayList<UserMealWithExcess>(), (list, item )-> list.add(meals.)
//                       )

             //   .collect(Collectors.groupingBy(UserMeal::getDateTime),Collectors.summingInt(UserMeal::getCalories))
////               ;
     //  return null;
    }
}
