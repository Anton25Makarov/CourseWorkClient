package by.bsuir.repository;

import by.bsuir.entities.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game> {
    private Map<Long, Game> data = new HashMap<>();

    @Override
    public void add(Game game) {
        long gameId = game.getId();
        data.put(gameId, game);
    }

    @Override
    public void remove(Game game) {
        long gameId = game.getId();
        data.remove(gameId);
    }

    @Override
    public void update(Game game) {
        long gameId = game.getId();
        if (data.containsKey(gameId)) {
            data.put(gameId, game);
        }
    }

    @Override
    public List<Game> sortBy(final Comparator comparator) {
        return data.values().stream().
                sorted((c1, c2) -> comparator.compare(c1, c2)).collect(Collectors.toList());
    }

    @Override
    public List<Game> findBy(Specification specification) {
        return data.values().stream().filter((s) -> specification.specified(s)).collect(Collectors.toList());
        
    }
}
