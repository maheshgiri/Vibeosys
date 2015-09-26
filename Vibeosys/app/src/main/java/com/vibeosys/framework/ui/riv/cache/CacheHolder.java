package com.vibeosys.framework.ui.riv.cache;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Vector;

/**
 * A Helper where all system-Wide Caches are registered by the Constructor of the {@link AbstractCache} class
 * 
 * @author Jan D.S. Wischweh <jwischweh@cellular.de>
 * 
 */

public class CacheHolder
{

    private static CacheHolder                   globalCacheHolder = null;

    @SuppressWarnings(value =
    { "rawtypes" })
    private Vector<WeakReference<AbstractCache>> caches            = new Vector<WeakReference<AbstractCache>>( 1 );

    public CacheHolder()
    {

    }

    /**
     * Registers a Cache in the Holder
     * 
     * @param c
     */

    @SuppressWarnings(value =
    { "rawtypes" })
    public void add( AbstractCache c )
    {
        caches.add( new WeakReference<AbstractCache>( c ) );
    }

    /**
     * Clears the holder from stale entries
     */
    @SuppressWarnings(value =
    { "rawtypes" })
    public void clean()
    {
        Iterator<WeakReference<AbstractCache>> cachesIterator = caches.iterator();
        while( cachesIterator.hasNext() )
        {
            WeakReference<AbstractCache> cacheRef = cachesIterator.next();
            if( cacheRef.get() == null )
            {
                cachesIterator.remove();
            }
        }
    }

    @SuppressWarnings(value =
    { "rawtypes" })
    /**
     * Clears all Caches known to the Holder
     */
    public void clearAllCaches()
    {
        Iterator<WeakReference<AbstractCache>> cachesIterator = caches.iterator();
        while( cachesIterator.hasNext() )
        {
            AbstractCache cache = cachesIterator.next().get();
            if( cache == null )
            {
                cachesIterator.remove();
            }
            else
            {
                cache.clear();
            }
        }
    }

    /**
     * Gets the Holder instance which holds all caches, that were constructed and are still in use (i.e. referenced somewhere).
     * 
     * @return
     */

    public static CacheHolder getGlobalHolder()
    {
        if( globalCacheHolder == null )
        {
            globalCacheHolder = new CacheHolder();
        }
        return globalCacheHolder;

    }

}
