package com.example.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import java.util.*;

public class DojoReactiveTest {

    @Test
    void converterData() {
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35() {
        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(list);

        observable.filter(jugador -> jugador.getAge() > 35)
                .subscribe(System.out::println);
    }

    @Test
    void jugadoresMayoresA35SegunClub() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.filter(player -> player.getAge() > 35)
                .distinct()
                .groupBy(Player::getClub)
                .flatMap(groupedFlux -> groupedFlux
                        .collectList()
                        .map(list -> {
                            Map<String, List<Player>> map = new HashMap<>();
                            map.put(groupedFlux.key(), list);
                            return map;
                        }))
                .subscribe(map -> {
                    map.forEach((key, value) -> {
                        System.out.println("\n");
                        System.out.println(key + ": ");
                        value.forEach(System.out::println);
                    });
                });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.filter(j -> j.getNational().equals("France"))
                .collect(Collectors.maxBy(Comparator.comparing(Player::getWinners)))
                // .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2)
                .subscribe(System.out::println);
    }

    @Test
    void clubsAgrupadosPorNacionalidad() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        Mono<Map<String, List<String>>> clubsPorNacionalidad = observable
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.mapping(Player::getClub, Collectors.toList())));

        clubsPorNacionalidad.subscribe(group -> {
            group.forEach((nacionalidad, clubes) -> {
                System.out.println("* País : " + nacionalidad + " -> ");
                clubes.forEach(club -> System.out.println("\t- Club : " + club));
                System.out.println("************************************");
            });
        });
    }

    @Test
    void clubConElMejorJugador() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable
                .collect(Collectors.maxBy(Comparator.comparing(Player::getWinners)))
                .subscribe(j -> {
                    System.out.println(j.get().getClub());
                });
    }

    @Test
    void clubConElMejorJugador2() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable
                .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2)
                .subscribe(j -> {
                    System.out.println(j.getClub());
                });
    }

    @Test
    void ElMejorJugador() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable
                .collect(Collectors.maxBy(Comparator.comparing(Player::getWinners)))
                .subscribe(System.out::println);
    }

    @Test
    void ElMejorJugador2() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable
                .reduce((jugador1, jugador2) -> jugador1.getWinners() >= jugador2.getWinners() ? jugador1 : jugador2)
                .subscribe(System.out::println);
    }

    @Test
    void mejorJugadorSegunNacionalidad() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.maxBy(Comparator.comparing(Player::getWinners))))
                .subscribe(group -> {
                    group.forEach((nacionalidad, jugador) -> {
                        System.out.println(
                                "* Nacionalidad: " + nacionalidad + ", Jugador: " + jugador.get().getName() + ", Club: "
                                        + jugador.get().getClub());
                    });
                });
    }
}
