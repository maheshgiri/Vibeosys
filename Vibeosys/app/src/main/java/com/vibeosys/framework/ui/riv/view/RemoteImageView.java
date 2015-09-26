package com.vibeosys.framework.ui.riv.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.vibeosys.framework.ui.riv.cache.ILoader;
import com.vibeosys.framework.ui.riv.cache.IMemoryManager;
import com.vibeosys.framework.ui.riv.cache.LeastRecentlyUsedCache;


public class RemoteImageView extends ImageView
{

    /* Maximum Size of the shared Cache */
    private static int                                  MAX_CACHE_SIZE                = 50;
    private static int                                  MIN_CACHE_SIZE                = 10;

    private static int                                  CORE_THREAD_POOL_SIZE         = 3;                                     // How many concurrent Threads do we want for image loading
    private static int                                  MAX_THREAD_POOL_SIZE          = CORE_THREAD_POOL_SIZE;
    private static int                                  KEEP_THREAD_ALIVE_TIME_IN_SEC = 2 * 60;

    public static final String                          DEBUG_TAG                     = "ImageView";

    private static final int                            WORKER_THREAD_PRIORITY        = Thread.NORM_PRIORITY - 1;

    protected ClearOldPlaceholderStrategy               clearPlaceHolder              = new ClearOldPlaceholderStrategy();
    protected ChangeNothingPlaceholderStrategy          changeNothing                 = new ChangeNothingPlaceholderStrategy();

    private Handler                                     uiHandler;

    private static IMemoryManager memoryManager                 = null;

    private static ThreadPoolExecutor                   mImageLoadingWorker           = null;

    private URL                                         mImageURL                     = null;
    private Bitmap                                      mBitmapResponse;
    private int                                         placeHolderResId              = -1;

    private IImageLoaded                                imageLoadListener;

    private PlaceHolderStrategy                         placeHolderStrategy;

    private boolean                                     renderImageOpaque;

    private ImageLoader                                 loader                        = new ImageLoader();

    private static final LeastRecentlyUsedCache<Bitmap> cache                         = new LeastRecentlyUsedCache<Bitmap>(
                                                                                              MAX_CACHE_SIZE, null );

    public static boolean isCached( String url )
    {
        return cache.isCached( url );

    }

    public static boolean isCached( URL url )
    {
        return isCached( url.toExternalForm() );
    }

    /**
     * Opens an url-connection and loads image
     * 
     * @param url
     *            url to load image from
     * @param opaque
     *            if true decoding uses optimzed color-space (without alpha) in order to save memory
     * @return the decoded Image
     * @throws IOException
     */
    protected static Bitmap decodeImageFromURL( URL url, boolean opaque ) throws IOException
    {
        URLConnection connection = url.openConnection();
        connection.setUseCaches( true );
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        if( opaque )
        {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        return BitmapFactory.decodeStream( connection.getInputStream(), null, options );
    }

    public static synchronized void addToCache( String url, Bitmap image )
    {
        cache.put( url, image );
    }

    public static void addToCache( URL url, Bitmap image )
    {
        addToCache( url.toExternalForm(), image );
    }

    /**
     * Trims the cache to the given size (may also be used to increase cache size) will respects {@link #MAX_CACHE_SIZE} and {@link #MIN_CACHE_SIZE}
     * 
     * @param size
     *            - new size of the cache
     */

    public static synchronized void trimCache( int size )
    {
        if( size < MIN_CACHE_SIZE )
            size = MIN_CACHE_SIZE;
        if( size > MAX_CACHE_SIZE )
            size = MAX_CACHE_SIZE;
        cache.setCapacity( size );
        Log.d( DEBUG_TAG, "New ImageCace capacity is " + cache.capacity() );
        System.gc();
        Thread.yield();
    }

    public static void trimCacheToHalf()
    {
        trimCache( cache.size() / 2 );
    }

    /**
     * @param context
     */
    public RemoteImageView( Context context )
    {
        super( context );
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public RemoteImageView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RemoteImageView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        // TODO Auto-generated constructor stub
    }

    protected static ThreadPoolExecutor getImageLoadingQueueWorker()
    {
        if( mImageLoadingWorker == null )
        {
            mImageLoadingWorker = new ThreadPoolExecutor( CORE_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE,
                    KEEP_THREAD_ALIVE_TIME_IN_SEC, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                    new ThreadFactory()
                    {
                        @Override
                        public Thread newThread( Runnable r )
                        {
                            Thread t = new Thread( r );
                            t.setPriority( WORKER_THREAD_PRIORITY );
                            return t;
                        }
                    } );

        }
        return mImageLoadingWorker;
    }

    protected void requestImage( final boolean assumeOpaque )
    {
        getImageLoadingQueueWorker().execute( new Runnable()
        {

            @Override
            public void run()
            {
                if( mImageURL != null )
                {
                    mBitmapResponse = cache.getFromCacheOrLoad( mImageURL.toExternalForm(), false, loader );
                    setImageOnUiThread();
                }
                else
                {
                    Log.w( DEBUG_TAG, "Warning imageUrl seems obsolete. Will skip Image loading" );
                }
            }
        } );

    }

    private void setImageOnUiThread()
    {

        if( uiHandler != null )
        {

            uiHandler.post( new SetImageRunnable() );
        }
        else
        {
            Log.w( DEBUG_TAG, "Could not Set Image: No reference to Activity and its ui-thread" );
        }
    }

    /**
     * Unlike the Original {@link android.widget.ImageView#setImageURI}; this Method will not block the ui-Thread but will load Images concurrent in a Thread shared by all RemoteImageViews
     * 
     */

    @Override
    public void setImageURI( Uri uri )
    {
        setImageURI( uri, clearPlaceHolder );

    }

    /**
     * Initalizes ImageLoading from remote Url. Uses the placeHolder Bitmap as long as the remote Image is not loaded
     * 
     * @param remoteUri
     * @param placeholder
     */
    public void setImageURI( Uri remoteUri, Bitmap placeholder )
    {
        setImageURI( remoteUri, new ShowDefaultBitmapAsPlaceHolderStrategy( placeholder ) );
    }

    /**
     * Initalizes ImageLoading from remote Url. Uses the Resource with the given id as long as the remote Image is not loaded
     * 
     * //@param remoteUri
     * //@param placeholder
     */
    public void setImageURI( Uri remoteUri, int placeholderResId )
    {
        setImageURI( remoteUri, new ShowDefaultResourceAsPlaceHolderStrategy( placeholderResId ) );
    }

    /**
     * Loads an Image in the background and displays it as soon as it is available
     * 
     * @param remoteUri
     *            the Uri of the Image to Load
     * //@param placeholderResId
     *            Resource ID for drawable that will be displayed until the remote Image was loaded
     */

    protected void setImageURI( Uri remoteUri, PlaceHolderStrategy defaultImage )
    {
        this.uiHandler = new Handler();
        this.placeHolderStrategy = defaultImage;

        if( remoteUri == null )
        {
            mImageURL = null;
            mBitmapResponse = null;
            updatePlaceHolder();
        }
        else
        {
            try
            {
                mImageURL = new URL( remoteUri.toString() );
                if( isCached( mImageURL.toExternalForm() ) )
                {
                    Log.d( DEBUG_TAG, "setImageURI from cache:" + mImageURL );
                    mBitmapResponse = cache.getFromCache( mImageURL.toExternalForm() );
                    updateImage();

                }
                else
                {
                    Log.d( DEBUG_TAG, "setImageURI not cached will load:" + mImageURL );
                    updatePlaceHolder();
                    requestImage( isOpaque() );
                }

            }
            catch( MalformedURLException e )
            {
                Log.w( DEBUG_TAG, "Unable to parse Uri->URL", e ); // Shouldn't occur
                updatePlaceHolder();
            }
        }
    }

    public void setRenderImageOpaque( boolean opaque )
    {
        renderImageOpaque = opaque;
    }

    protected static void freeImageMemory()
    {
        cache.clear();
        if( memoryManager != null )
        {
            memoryManager.freeMemory();
        }

    }

    public class ImageLoader implements ILoader<Bitmap>
    {

        @Override
        public Bitmap load( String id )
        {
            Bitmap result = null;
            try
            {
                result = decodeImageFromURL( new URL( id ), renderImageOpaque );
            }
            catch( OutOfMemoryError e )
            {
                freeImageMemory();
                try
                {
                    Log.d( DEBUG_TAG, "Out of Memory during decoding of " + id
                            + " will try to free Memory and try again", e );
                    result = decodeImageFromURL( new URL( id ), renderImageOpaque );
                }
                catch( OutOfMemoryError e2 )
                {
                    Log.e( DEBUG_TAG, "Still no memory on second try of decoding " + id + ". Giving up", e2 );
                }
                catch( IOException e2 )
                {
                    Log.e( DEBUG_TAG, "Error while decoding Image from " + id, e2 );
                }
            }
            catch( IOException e )
            {
                Log.e( DEBUG_TAG, "Error while decoding Image from " + id, e );
            }
            return result;
        }

    }

    protected void updatePlaceHolder()
    {
        if( this.placeHolderStrategy != null )
        {
            this.placeHolderStrategy.apply();
        }
    }

    public static void setOnLowMemoryHandler( IMemoryManager lowMemoryHandler )
    {
        memoryManager = lowMemoryHandler;
    }

    /**
     * Call inside ui-thread
     */
    protected void updateImage()
    {
        if( mBitmapResponse != null )
        {
            setImageBitmap( mBitmapResponse );
        }
        else if( placeHolderResId != -1 )
        {
            setImageResource( placeHolderResId );
        }
        else
        {
            setImageDrawable( null );
        }
        notifyImageLoadListener();
    }

    /**
     * Listener will be called after image was loaded and set and inside the uithread
     * 
     * @note Signature will change soon, to provide more callback information
     * @param listener
     */
    public void setImageLoadListener( IImageLoaded listener )
    {
        this.imageLoadListener = listener;

    }

    private void notifyImageLoadListener()
    {
        if( this.imageLoadListener != null )
        {
            imageLoadListener.onImageLoaded( this );
        }

    }

    public class SetImageRunnable implements Runnable
    {

        @Override
        public void run()
        {
            updateImage();
        }

    }

    //
    // Implementations of different PlaceHolder-Strategies. i.e. what will be shown until the remote Image is loaded
    //

    private abstract class PlaceHolderStrategy
    {
        /**
         * Will be called if a new ImageUrl has been set, and the Image still has to be loaded Implemet your default behavior here.
         */
        protected abstract void apply();
    }

    private class ShowDefaultBitmapAsPlaceHolderStrategy extends PlaceHolderStrategy
    {

        Bitmap defaultBitmap;

        public ShowDefaultBitmapAsPlaceHolderStrategy( Bitmap defaultBitmap )
        {
            super();
            this.defaultBitmap = defaultBitmap;
        }

        @Override
        protected void apply()
        {
            setImageBitmap( defaultBitmap );
        }
    }

    private class ShowDefaultResourceAsPlaceHolderStrategy extends PlaceHolderStrategy
    {

        int resId;

        public ShowDefaultResourceAsPlaceHolderStrategy( int resId )
        {
            super();
            this.resId = resId;
        }

        @Override
        protected void apply()
        {
            setImageResource( this.resId );
        }
    }

    private class ChangeNothingPlaceholderStrategy extends PlaceHolderStrategy
    {
        @Override
        protected void apply()
        {

        }
    }

    private class ClearOldPlaceholderStrategy extends PlaceHolderStrategy
    {
        @Override
        protected void apply()
        {
            setImageBitmap( null );
        }
    }

}
