package by.bsuir.course.builder;

import by.bsuir.course.entities.Address;
import by.bsuir.course.entities.Referee;

public class RefereeBuilder {
    private String login = "default login";
    private String password = "default password";
    private String sport = "Фигурное катание";
    private int age = 18;
    private String name = "default name";
    private String surname = "default surname";
    private String country = "Беларусь";
    private String city = "Минск";

    public RefereeBuilder buildLogin(String login) {
        this.login = login;
        return this;
    }

    public RefereeBuilder buildPassword(String password) {
        this.password = password;
        return this;
    }

    public RefereeBuilder buildSport(String sport) {
        this.sport = sport;
        return this;
    }

    public RefereeBuilder buildAge(int age) {
        this.age = age;
        return this;
    }

    public RefereeBuilder buildName(String name) {
        this.name = name;
        return this;
    }

    public RefereeBuilder buildSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public RefereeBuilder buildCountry(String country) {
        this.country = country;
        return this;
    }

    public RefereeBuilder buildCity(String city) {
        this.city = city;
        return this;
    }

    public Referee build() {
        Referee referee = new Referee();
        referee.setLogin(login);
        referee.setPassword(password);
        referee.setSport(sport);
        referee.setName(name);
        referee.setSurname(surname);
        referee.setAge(age);
        Address address = new Address();
        address.setCountry(country);
        address.setCity(city);
        referee.setAddress(address);

        return referee;
    }
}
