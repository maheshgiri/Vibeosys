package com.vibeosys.framework.ui.riv.cache;

import android.util.Log;

/**
 * Abstract Class for all Cache-Strategies
 * 
 * The Cache implementation maintains and manages the cached objects according to its cache strategy.
 * 
 * Each Cache Entry is identified by a String key. The Type of Objects in Cache is Generic <T>
 * 
 * The cached can be configured to automaticaly load uncached objects from other sources by passing an appropriate Implementation of an @see {@link ILoader}. In which case the cache should call the ILoader if an object is requested but not present
 * 
 * All caches are automatically registered to a global CacheHolder @see {@link CacheHolder#getGlobalHolder()} which can be used for clean-up and memory management purposes.
 * 
 * @author Jan Wischweh <jwischweh@cellular.de>
 * 
 * @param <T>
 *            Type of Objects you want to store in this Cache
 */

public abstract class AbstractCache<T>
{

    public String      DEBUG_TAG = "CACHE";

    private ILoader<T> defaultLoader;

    /**
     * Creates a new Cache and registers it in the global CacheHolder
     * 
     * @param loader
     *            - The loader to use for loading objects into cache
     */
    public AbstractCache( ILoader<T> loader )
    {
        this.defaultLoader = loader;
        CacheHolder.getGlobalHolder().add( this );
    }

    /**
     * Returns the loader which this Cache uses to load Objects which are not present in the cache
     * 
     * @return
     */

    public ILoader<T> getDefaultLoader()
    {
        return this.defaultLoader;
    }

    /**
     * Tries to obtain an object from Cache. Uses the default loader (if present) as fallback.
     * 
     * @param id
     * @param forceReload
     *            - if true doesnt use cache but forces reloading
     * @return the cached or loaded object or null if no such
     */

    public T getFromCacheOrLoad( String key, boolean forceReload )
    {
        return getFromCacheOrLoad( key, forceReload, getDefaultLoader() );
    }

    /**
     * Tries to obtain an object from Cache. Uses the given loader (if present) as fallback
     * 
     * @param key
     * @param forceReload
     * @param loader
     * @return
     */
    public T getFromCacheOrLoad( String key, boolean forceReload, ILoader<T> loader )
    {
        if( forceReload || !isCached( key ) )
        {
            T loadedObject = null;
            if( loader != null )
            {
                loadedObject = loader.load( key );

            }
            else
            {
                Log.w( DEBUG_TAG, "No object Cached and no loader present for object: " + key );
            }
            if( loadedObject != null )
            {
                Log.d( DEBUG_TAG, "Loaded " + key );
                put( key, loadedObject );
            }
            else
            {
                Log.w( DEBUG_TAG, "Could not load object: " + key );
            }

            return loadedObject;
        }
        else
        {
            Log.d( DEBUG_TAG, "Retrieved from cache: " + key );
            return getFromCache( key );
        }
    }

    /**
     * 
     * @param id
     * @return true if object is in cache and is not null
     */
    public boolean isCached( String key )
    {
        T obj = getFromCache( key );
        return obj != null;
    }

    /**
     * Implemet this to return Objects from cache or null if object is not in cache; should call {@link #recordCacheHit(String)} on Cache Hits
     * 
     * @param id
     * @return
     */
    public abstract T getFromCache( String key );

    /**
     * Implement this to record Cache hits should be called by your implementation of {@link #getFromCache(String)}
     * 
     * @param id
     */
    protected abstract void recordCacheHit( String key );

    /**
     * Implement this to put Objects into Cache
     * 
     * @param id
     * @param objectToCache
     */
    public abstract void put( String key, T objectToCache );

    /**
     * Implement this to remove Objects from cache
     * 
     * @param key
     */
    public abstract void remove( String key );

    /**
     * Implement this to provide cacheFunctionality
     */
    public abstract void clear();

    /**
     * Implement this to handle Capacity changes
     * 
     * @param capacity
     */
    public abstract void setCapacity( int capacity );

    /**
     * Returns the maximun cache capacity
     * 
     * @return
     */
    public abstract int capacity();

    /**
     * Returns the current cache size. I.e. number of objects in cache
     * 
     * @return
     */
    public abstract int size();

}
