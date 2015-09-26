package com.vibeosys.framework.ui.riv.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.util.Log;

/**
 * Implements a Cache with LeastRecentlyUsedCache
 * 
 * The cache also holds elements which leave the LeastRecentyUsed-Pipe as SoftReference in a backup-Cache
 * 
 * @author Jan D.S. Wischweh <jwischweh@cellular.de>
 * 
 * @param <T>
 *            Type of Cache Values
 */
public class LeastRecentlyUsedCache<T> extends AbstractCache<T>
{

    private int                   capacity         = 10;

    private SoftReferenceCache<T> backupReferences = new SoftReferenceCache<T>( null );
    private LeastRecentlyUsedMap  cache            = new LeastRecentlyUsedMap( capacity );

    /**
     * 
     * @param capacity
     *            maximum capacity
     * @param loader
     *            Loader to use, if object requested but not present
     */

    public LeastRecentlyUsedCache( int capacity, ILoader<T> loader )
    {
        super( loader );
        setCapacity( capacity );
    }

    /**
     * Retrieves an Object from cache
     * 
     * @param key
     *            id of the object to retrieve
     * @return object in cache or null if none cached
     */
    @Override
    public synchronized T getFromCache( String key )
    {
        T result = cache.get( key );
        if( result == null )
        {
            result = backupReferences.getFromCache( key );
            if( result != null )
            {
                Log.d( DEBUG_TAG,
                        " Retrieved object from SoftRefernce backup cache: "
                                + key );
                cache.put( key, result ); // We put this back into our cache
            }
        }
        // No need to call recordCacheHit
        return result;
    }

    /**
     * Registers a cache hit
     */
    @Override
    protected synchronized void recordCacheHit( String key )
    {
        cache.get( key );

    }

    /**
     * Puts an object into the cache.
     * 
     * If you put in null the cache entry will be deleted
     */
    @Override
    public synchronized void put( String key, T objectToCache )
    {
        if( objectToCache == null )
        {
            remove( key );
            return;
        }
        cache.put( key, objectToCache );

    }

    /**
     * Removes object from Cache
     */

    @Override
    public synchronized void remove( String key )
    {
        cache.remove( key );
        backupReferences.remove( key );

    }

    /**
     * Clears the cache and the backup cache
     */
    @Override
    public synchronized void clear()
    {
        cache.clear();
        clearSoftRefernceBackupCache();

    }

    /**
     * Clears the SoftReference Backup Cache.
     */
    public void clearSoftRefernceBackupCache()
    {
        backupReferences.clear();
    }

    /**
     * Changes the capacity of the cache;
     */
    @Override
    public synchronized void setCapacity( int capacity )
    {
        if( capacity == capacity() )
        {
            return; // Nothing to do
        }

        LeastRecentlyUsedMap newCache = new LeastRecentlyUsedMap( capacity );
        Iterator<Entry<String, T>> cacheIter = cache.entrySet().iterator();
        for( int i = 0; i < capacity && cacheIter.hasNext(); i++ )
        {
            Entry<String, T> entry = cacheIter.next();
            newCache.put( entry.getKey(), entry.getValue() );
        }
        cache.clear();
        cache = newCache;
        this.capacity = capacity;

    }

    @Override
    public int size()
    {
        return cache.size();
    }

    @Override
    public int capacity()
    {
        return capacity;
    }

    /**
     * Implementation of {@link LinkedHashMap} which puts elements that drop out into the SoftRefernece backup cache
     * 
     * @author wischweh
     * 
     */
    private class LeastRecentlyUsedMap extends LinkedHashMap<String, T>
    {

        private static final long serialVersionUID = -2221644032453858268L;

        public LeastRecentlyUsedMap( int maxCapacity )
        {
            super( maxCapacity, 0.75f, true );
        }

        @Override
        protected boolean removeEldestEntry( Entry<String, T> eldest )
        {

            if( size() > capacity() )
            {
                backupReferences.put( eldest.getKey(), eldest.getValue() );
                Log.d( DEBUG_TAG, "Moving " + eldest.getKey()
                        + " to SoftRefernce Backup Cache. Cache usage= "
                        + size() + "/" + capacity() );
                return true;
            }
            else
            {
                return false;
            }

        }

    }

}
