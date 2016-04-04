package t14.com.GameRentals;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.ListView;

/**
 * Created by aredmond on 3/10/16.
 */
public class SearchGameActivityTest extends ActivityInstrumentationTestCase2 {


    public SearchGameActivityTest() {super(SearchGameActivity.class);}

    public void testActivityExists() {
        Intent intent = new Intent();
        intent.putExtra("SEARCH_TERM", "rpg");
        setActivityIntent(intent);


        SearchGameActivity activity = (SearchGameActivity) getActivity();
        assertNotNull(activity);


        Game EUIV = new Game("EUIV", "grand strategy", "Austin");
        EUIV.setStatus(GameController.STATUS_BIDDED);
    }

    public void testViewOnScreen(){
        Intent intent = new Intent();
        intent.putExtra("SEARCH_TERM", "rpg");
        setActivityIntent(intent);

        SearchGameActivity searchGameActivity = (SearchGameActivity) getActivity();

        ListView listView = (ListView) searchGameActivity.findViewById(R.id.ReturnedGamesView);

        ViewAsserts.assertOnScreen(searchGameActivity.getWindow().getDecorView(), listView);
    }

    public void testSearch() {
        User Austin = new User("Austin", "aredmond@ualberta.ca", "780");
        Game EUIV = new Game("EUIV", "grand strategy", "Austin");

        Intent intent = new Intent();
        intent.putExtra("SEARCH_TERM", "grand");
        setActivityIntent(intent);

        SearchGameActivity activity = (SearchGameActivity) getActivity();
        boolean hasGame = false;
        for (Game game: activity.getReturnedGames()) {
            if(EUIV.getGameName().equals(game.getGameName())) {
                hasGame = true;
            }
        }
        assertTrue(hasGame);
    }


}
