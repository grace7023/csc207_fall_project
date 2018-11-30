package fall2018.csc2017.game_centre.sliding_tiles;

import java.util.ArrayList;
import java.util.List;

public class STGameTest {

    private List<STTile> sortedTileList(int boardSize) {
        List<STTile> tiles = new ArrayList<>();
        for (int i = 0; i < boardSize * boardSize; i++) {
            tiles.add(new STTile(i, boardSize));
        }
        return tiles;
    }
}
