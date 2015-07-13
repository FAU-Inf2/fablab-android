package de.fau.cs.mad.fablab.android.viewmodel.common;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is taken from the kwikshop project
 *
 * Arraylist that can be monitored for changes
 *
 * @param <T> The type of items the list holds
 */
public class ObservableArrayList<T> extends ArrayList<T> {


    /**
     * Listener interface for observers to implement
     *
     * @param <T> The type of items the list being observed contains
     */
    public interface Listener<T> {

        /**
         * Called after an item has been added to the list
         *
         * @param newItem The item that was added to the list
         */
        void onItemAdded(T newItem);

        /**
         * Called after a collection of items has been added to the list
         *
         * @param collection The collection of items that was added to the list
         */

        void onAllItemsAdded(Collection<? extends T> collection);

        /**
         * Called after an item has been removed from the list
         *
         * @param removedItem The item that was removed from the list
         */
        void onItemRemoved(T removedItem);

    }


    Listener<T> listener;           //current listener


    public ObservableArrayList() {
        super();
    }

    public ObservableArrayList(Collection<? extends T> c) {
        super(c);
    }


    public void setListener(final Listener<T> value) {
        this.listener = value;
    }

    @Override
    public void add(int location, T object) {
        super.add(location, object);
        if (listener != null) {
            listener.onItemAdded(object);
        }
    }

    @Override
    public boolean add(T object) {
        boolean success = super.add(object);
        if (success && listener != null) {
            listener.onItemAdded(object);
        }
        return success;
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        boolean success = super.addAll(location, collection);
        if (success && listener != null) {
            listener.onAllItemsAdded(collection);
        }
        return success;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean success = super.addAll(collection);
        if (success && listener != null) {
            listener.onAllItemsAdded(collection);
        }
        return success;
    }

    @Override
    public void clear() {
        if (listener != null) {
            List<T> allItems = new ArrayList<>(this);
            super.clear();
            for (T item : allItems) {
                listener.onItemRemoved(item);
            }
        } else {
            super.clear();
        }
    }


    @NonNull
    @Override
    public Iterator<T> iterator() {

        final Iterator<T> baseIterator = super.iterator();
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return baseIterator.hasNext();
            }

            @Override
            public T next() {
                return baseIterator.next();
            }

            @Override
            public void remove() {
                //no supported as we're not able to fire the right event without a specialized
                // iterator implementation
                throw new UnsupportedOperationException();
            }
        };
    }


    @NonNull
    @Override
    public ListIterator<T> listIterator() {

        return new ReadonlyListIteratorWrapper(super.listIterator());
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return new ReadonlyListIteratorWrapper(super.listIterator(location));
    }

    @Override
    public T remove(int location) {
        T removedItem = super.remove(location);
        if (listener != null) {
            listener.onItemRemoved(removedItem);
        }
        return removedItem;
    }

    @Override
    public boolean remove(Object object) {
        boolean success = super.remove(object);
        if (success && listener != null) {
            try {
                listener.onItemRemoved((T) object);
            } catch (ClassCastException ex) {
                //do not fire event if the object we removed from the list can't be cast to T
                // (it should have been there in the first place)
                //no idea why java offers remove(Object) instead of remove(T)
            }
        }
        return success;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int location, T object) {
        T oldItem = super.set(location, object);
        if (listener != null) {
            listener.onItemRemoved(oldItem);
            listener.onItemAdded(object);
        }
        return oldItem;
    }

    private class ReadonlyListIteratorWrapper implements ListIterator<T> {

        private ListIterator<T> wrappedIterator;

        public ReadonlyListIteratorWrapper(ListIterator<T> wrappedIterator) {
            this.wrappedIterator = wrappedIterator;
        }

        @Override
        public void add(T object) {
            wrappedIterator.add(object);
            if (listener != null) {
                listener.onItemAdded(object);
            }
        }

        @Override
        public boolean hasNext() {
            return wrappedIterator.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return wrappedIterator.hasPrevious();
        }

        @Override
        public T next() {
            return wrappedIterator.next();
        }

        @Override
        public int nextIndex() {
            return wrappedIterator.nextIndex();
        }

        @Override
        public T previous() {
            return wrappedIterator.previous();
        }

        @Override
        public int previousIndex() {
            return wrappedIterator.previousIndex();
        }

        @Override
        public void remove() {
            //no supported as we're not able to fire the right event without a specialized
            // iterator implementation
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T object) {
            //no supported as we're not able to fire the right event without a specialized
            // iterator implementation
            throw new UnsupportedOperationException();
        }
    }

}
