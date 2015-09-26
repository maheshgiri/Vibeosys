package com.vibeosys.framework.ui.riv.cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

/**
 * A Cache which uses SoftReferences. Objects are removed from cache either if capacity is exceeded or they are no longer referenced and the system needs memory.
 * 
 * @author wischweh
 * 
 * @param <T>
 */
public class SoftReferenceCache<T> extends AbstractCache<T>
{

    private int                                         capacity = 2000;
    private ConcurrentHashMap<String, SoftReference<T>> cache    = new ConcurrentHashMap<String, SoftReference<T>>( 20 );

    public SoftReferenceCache( ILoader<T> loader )
    {
        super( loader );

    }

    /**
     * Retrieves Object from Cache
     * 
     * @param key
     *            id of the Object
     * @return Object or null if none cached
     */
    @Override
    public T getFromCache( String key )
    {
        SoftReference<T> ref = cache.get( key );
        if( ref == null )
        {
            return null;
        }
        else
        {
            recordCacheHit( key );
            return ref.get();
        }

    }

    /**
     * Cache Hits are irrelevant for this Cache, therefore does nothing
     */
    @Override
    protected void recordCacheHit( String key )
    {
        // Nothing todo

    }

    /**
     * puts an object into the Cache
     * 
     * @param key
     *            the id of the objects
     * @param objectToCache
     *            object to cache
     */
    @Override
    public void put( String key, T objectToCache )
    {
        if( objectToCache == null )
        {
            Log.d( DEBUG_TAG,
                    "placing null into cache is ignored. If you want to remove an element use removeFromCache(obj)" );
            return;
        }

        cache.put( key, new SoftReference<T>( objectToCache ) );
        if( cache.size() > capacity || cache.size() % 100 == 0 )
        { // Clean if capacity exceeded or every 100th call
            clean();
        }

    }

    /**
     * Clears the cache
     */

    @Override
    public void clear()
    {
        cache.clear();

    }

    /**
     * Changes the capacity of the cache.
     * 
     * As we use {@link SoftReference}s in this cache capacity should be large.
     * 
     */
    @Override
    public void setCapacity( int capacity )
    {
        this.capacity = capacity;
        clean();

    }

    /**
     * Cleans up the cache. Trims cache to capacity and removes stale references
     * 
     */
    protected void clean()
    {
        if( capacity == 0 )
        {
            clear();
        }
        else
        {
            cleanStaleReferences();
            while( cache.size() > capacity )
            {
                cache.remove( cache.keys().nextElement() );
            }
        }
    }

    /**
     * Removes Cache-Entries for objects whichs {@link SoftReference}s are stale
     */
    protected void cleanStaleReferences()
    {
        Iterator<Entry<String, SoftReference<T>>> refIter = cache.entrySet().iterator();
        while( refIter.hasNext() )
        {
            Entry<String, SoftReference<T>> entry = refIter.next();
            if( entry.getValue().get() == null )
            {
                refIter.remove();
            }
        }

    }

    /**
     * Return number of objects in cache. Due to the nature of this cache, reported number may be too large, as the cache may hold stale refrences
     */
    public int size()
    {
        return cache.size();
    }

    /**
     * Removes Object from cache
     * 
     * @param key
     *            id of the Object to remove
     */

    public void remove( String key )
    {
        cache.remove( key );

    }

    /**
     * Returns maximum capacity of this cache
     */
    @Override
    public int capacity()
    {
        return capacity;
    }
}
