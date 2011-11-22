package com.app.augmentedbizz.application.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.ui.renderer.OpenGLModel;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;
import com.app.augmentedbizz.ui.renderer.Texture;
import com.app.augmentedbizz.util.TypeConversion;

/**
 * Database adapter class that implements the interface between the physical database
 * and the abstract object model. It provides methods to read from and write to the
 * local cache database.
 * 
 * @author Miffels
 *
 */
public class CacheDbAdapter {

	// Column names
    public static final String KEY_ID = "id";
    public static final String KEY_VERSION = "version";
    public static final String KEY_VERTICES= "vertices";
    public static final String KEY_NORMALS = "normals";
    public static final String KEY_TEXTURE_COORDS = "texture_coords";
    public static final String KEY_INDICES = "indices";
    public static final String KEY_TEXTURE = "texture";
    public static final String KEY_TEXTURE_WIDTH = "texture_width";
    public static final String KEY_TEXTURE_HEIGHT = "texture_height";
    public static final String KEY_SCALE_FACTOR = "scale_factor";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    // Db name, version and table name
    private static final String DATABASE_NAME = "arbizz";
    private static final String DATABASE_TABLE = "models";
    private static final int DATABASE_VERSION = 3;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
    		"CREATE TABLE " + CacheDbAdapter.DATABASE_TABLE + " (" +
        		CacheDbAdapter.KEY_ID + " INTEGER PRIMARY KEY, " +
        		CacheDbAdapter.KEY_VERSION + " INTEGER NOT NULL, " +
        		CacheDbAdapter.KEY_VERTICES + " BLOB NOT NULL, " +
        		CacheDbAdapter.KEY_NORMALS + " BLOB NOT NULL, " +
        		CacheDbAdapter.KEY_TEXTURE_COORDS + " BLOB NOT NULL, " +
        		CacheDbAdapter.KEY_INDICES + " BLOB NOT NULL, " +
        		CacheDbAdapter.KEY_TEXTURE + " BLOB NOT NULL, " +
        		CacheDbAdapter.KEY_TEXTURE_WIDTH + " INT NOT NULL, " +
        		CacheDbAdapter.KEY_TEXTURE_HEIGHT + " INT NOT NULL, " +
        		CacheDbAdapter.KEY_SCALE_FACTOR + " REAL NOT NULL);";
    
    /**
     * Database deletion SQL statement
     */
    private static final String DATABASE_DROP =
    		"DROP TABLE IF EXISTS models;";

    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context,
            		CacheDbAdapter.DATABASE_NAME,
            		null,
            		CacheDbAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CacheDbAdapter.DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            DebugLog.logw("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(CacheDbAdapter.DATABASE_DROP);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public CacheDbAdapter(Context context) {
        this.context = context;
    }

    /**
     * Open the model database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure.
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public CacheDbAdapter open() throws SQLException {
        this.dbHelper = new DatabaseHelper(this.context);
        this.db = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    /**
     * Create a new model using the information provided. If the model is
     * successfully created return the new rowId for that model, otherwise return
     * a -1 to indicate failure.
     * 
     * @param model The model information to insert.
     * @return rowId or -1 if failed
     */
    public long insertModel(OpenGLModelConfiguration model) {
        ContentValues initialValues = this.getContentValuesFrom(model);
        
        long result = this.db.insert(CacheDbAdapter.DATABASE_TABLE, null, initialValues);
        
        if(result == -1) {
        	DebugLog.loge("SQL failure: Could not insert model record no. " + model.getOpenGLModel().getId() + ".");
        }

        return result;
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of model to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteModelAt(long rowId) {
    	
    	long result = db.delete(CacheDbAdapter.DATABASE_TABLE,
    			CacheDbAdapter.KEY_ID + "=" + rowId, null);
    	
    	if(result == -1) {
        	DebugLog.loge("SQL failure: Could not delete model record no. " + rowId + ".");
        }

        return result != -1;
    }

    /**
     * Return a Cursor over the list of all models in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllModels() {
        return db.query(CacheDbAdapter.DATABASE_TABLE,
        		this.getDatabaseTableColumnNamesAsStringArray(),
        		null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the model that matches the given rowId
     * 
     * @param rowId id of model to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public OpenGLModelConfiguration fetchModel(long id) throws SQLException {
    	
        Cursor cursor = this.db.query(true,
        	CacheDbAdapter.DATABASE_TABLE,
        	this.getDatabaseTableColumnNamesAsStringArray(),
            CacheDbAdapter.KEY_ID + "=" + id,
            null, null, null, null, null);
        
        if (cursor != null && cursor.getCount() > 0) {
        	cursor.moveToFirst();
        } else {
        	return null;
        }
        
        Texture texture = new Texture(cursor.getInt(7),
        		cursor.getInt(8),
        		cursor.getBlob(6));
        
        OpenGLModelConfiguration openGLModelConfiguration = new OpenGLModelConfiguration(new OpenGLModel(
        		cursor.getInt(0),
        		cursor.getInt(1),
        		TypeConversion.toFloatArrayFrom(cursor.getBlob(2)),
        		TypeConversion.toFloatArrayFrom(cursor.getBlob(3)),
        		TypeConversion.toFloatArrayFrom(cursor.getBlob(4)),
        		TypeConversion.toShortArrayFrom(cursor.getBlob(5)),
        		texture), cursor.getFloat(9));
        
        cursor.close();
        
        return openGLModelConfiguration;
    }
    
    /**
     * Checks whether a model exists already in the database or not.
     * 
     * @param id The model Id
     * @return true, if the model with the specified id is stored in the cache
     */
    public boolean isModelExisting(long id) {
    	Cursor cursor = this.db.query(true,
            	CacheDbAdapter.DATABASE_TABLE,
            	this.getDatabaseTableColumnNamesAsStringArray(),
                CacheDbAdapter.KEY_ID + "=" + id,
                null, null, null, null, null);
            
            if (cursor == null || cursor.isAfterLast()) {
            	return false;
            } else {
            	return true;
            }
    }
    
    /**
     * @return
     */
    public boolean isOpen() {
    	return this.db != null && this.db.isOpen();
    }
    
    /**
     * Update the {@link OpenGLModel} using the details provided. The model to be updated is
     * specified using the rowId, and it is altered to use the values passed in. This function
     * expects a complete model, missing information will delete existing contents!
     * 
     * @param model The model information that should be inserted
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateModel(OpenGLModelConfiguration model) {
        ContentValues contentValues = this.getContentValuesFrom(model);

        return this.db.update(CacheDbAdapter.DATABASE_TABLE,
        		contentValues,
        		CacheDbAdapter.KEY_ID + "=" + model.getOpenGLModel().getId(),
        		null) > 0;
    }
    
    /**
     * Creates a set of {@link ContentValue}s as they are needed for
     * database insertions/updates from an {@link OpenGLModel}.
     * 
     * @param model The model to create the content values from.
     * @return A set of content values containing the model information.
     */
    private ContentValues getContentValuesFrom(OpenGLModelConfiguration model) {
    	ContentValues contentValues = new ContentValues();
    	
    	contentValues.put(CacheDbAdapter.KEY_ID,
        		model.getOpenGLModel().getId());
    	contentValues.put(CacheDbAdapter.KEY_VERSION,
        		model.getOpenGLModel().getModelVersion());
    	contentValues.put(CacheDbAdapter.KEY_VERTICES,
        		TypeConversion.toByteArrayFrom(model.getOpenGLModel().getVertices()));
    	contentValues.put(CacheDbAdapter.KEY_NORMALS,
        		TypeConversion.toByteArrayFrom(model.getOpenGLModel().getNormals()));
    	contentValues.put(CacheDbAdapter.KEY_TEXTURE_COORDS,
        		TypeConversion.toByteArrayFrom(model.getOpenGLModel().getTextureCoordinates()));
    	contentValues.put(CacheDbAdapter.KEY_INDICES,
        		TypeConversion.toByteArrayFrom(model.getOpenGLModel().getIndices()));
    	contentValues.put(CacheDbAdapter.KEY_TEXTURE,
        		model.getOpenGLModel().getTexture().getData());
    	contentValues.put(CacheDbAdapter.KEY_TEXTURE_WIDTH,
        		model.getOpenGLModel().getTexture().getWidth());
    	contentValues.put(CacheDbAdapter.KEY_TEXTURE_HEIGHT,
        		model.getOpenGLModel().getTexture().getHeight());
    	contentValues.put(CacheDbAdapter.KEY_SCALE_FACTOR,
        		model.getPreferredScaleFactor());
        
        return contentValues;
    }
    
    /**
     * Returns a complete list of the arbizz database tables column names
     * as needed for select statements.
     * 
     * @return A complete list of the arbizz database table column names.
     */
    private String[] getDatabaseTableColumnNamesAsStringArray() {
    	return new String[] {CacheDbAdapter.KEY_ID,
               	CacheDbAdapter.KEY_VERSION,
               	CacheDbAdapter.KEY_VERTICES,
               	CacheDbAdapter.KEY_NORMALS,
               	CacheDbAdapter.KEY_TEXTURE_COORDS,
               	CacheDbAdapter.KEY_INDICES,
               	CacheDbAdapter.KEY_TEXTURE,
               	CacheDbAdapter.KEY_TEXTURE_WIDTH,
               	CacheDbAdapter.KEY_TEXTURE_HEIGHT,
               	CacheDbAdapter.KEY_SCALE_FACTOR};
    }
	
}
