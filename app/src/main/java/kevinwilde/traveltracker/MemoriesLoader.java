package kevinwilde.traveltracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Kevin Wilde on 12/27/2015.
 */
public class MemoriesLoader extends DbCursorLoader {

    private MemoriesDataSource mDataSource;

    public MemoriesLoader(Context context, MemoriesDataSource dataSource) {
        super(context);
        mDataSource = dataSource;
    }

    @Override
    protected Cursor loadCursor() {
        return mDataSource.allMemoriesCursor();
    }
}
