package com.example.demo;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData() {
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35() {
        List<Player> list = CsvUtilFile.getPlayers();
        Set<Player> result = list.stream()
                .filter(jugador -> jugador.getAge() > 35)
                .collect(Collectors.toSet());
        result.forEach(System.out::println);
    }

    @Test
    void jugadoresMayoresA35SegunClub() {
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<Player>> result = list.stream()
                .filter(player -> player.getAge() > 35)
                .distinct()
                .collect(Collectors.groupingBy(Player::getClub));

        result.forEach((key, jugadores) -> {
            System.out.println("\n");
            System.out.println(key + ": ");
            jugadores.forEach(System.out::println);
        });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia() {
        List<Player> list = CsvUtilFile.getPlayers();

        Optional<Player> mejorJugador = list.stream()
                .filter(j -> j.getNational().equals("France"))
                .max(Comparator.comparing(Player::getWinners));
                // .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2);

        System.out.println(mejorJugador.orElse(null));
    }

    @Test
    void clubsAgrupadosPorNacionalidad() {
        List<Player> list = CsvUtilFile.getPlayers();

        Map<String, List<String>> clubsPorNacionalidad = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.mapping(Player::getClub, Collectors.toList())));

        clubsPorNacionalidad.forEach((nacionalidad, clubes) -> {
            System.out.println("* País : " + nacionalidad + " -> ");
            clubes.forEach(club -> System.out.println("\t- Club : " + club));
            System.out.println("************************************");
        });
    }

    @Test
    void clubConElMejorJugador() {
        List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = list.stream()
                .max(Comparator.comparing(Player::getWinners));
                // .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2);

        System.out.println("Club: " + mejorJugador.get().getClub());
    }

    @Test
    void ElMejorJugador() {
        List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = list.stream()
                .max(Comparator.comparing(Player::getWinners));
                // .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2);

        System.out.println("Name: " + mejorJugador.get().getName()
                + ", Winners:" + mejorJugador.get().getWinners()
                + ", Club: " + mejorJugador.get().getClub()
                + ", Nacionalidad: " + mejorJugador.get().getNational());
    }

    @Test
    void mejorJugadorSegunNacionalidad() {
        List<Player> list = CsvUtilFile.getPlayers();

        Map<String, Optional<Player>> mejoresJugadoresPorNacionalidad = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.maxBy(Comparator.comparing(Player::getWinners))));

        mejoresJugadoresPorNacionalidad.forEach((nacionalidad, jugador) -> {
            System.out.println("* Nacionalidad: " + nacionalidad + ", Jugador: " + jugador.get().getName() + ", Club: "
                    + jugador.get().getClub());
        });
    }

}
