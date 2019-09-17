import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyListImpl implements MyList, ListIterable {
    private Object[] elementData;
    private static final int DEFAULT_CAPACITY = 1;
    private int size;


    public MyListImpl (int initialCapacity) {
        elementData = new Object[initialCapacity];

    }

    public MyListImpl() {
        this(DEFAULT_CAPACITY);
    }



    @Override
    public void add(Object e) {
        if(size == elementData.length) {
            Object[] temp = new Object[elementData.length + DEFAULT_CAPACITY];
            for(int i = 0; i < elementData.length; i++) {
                temp[i] = elementData[i];
            }
            elementData = temp;
        }
        elementData[size] = e;
        size++;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < toArray().length; i++) {
            if (toArray()[i] == null) {
                builder.append("[" + "null" + "],");
                continue;
            }
            builder.append(" результат вызова toString для элемента "+ i+": " + toArray()[i].toString() + " ,");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void clear() {
        elementData = new Object[DEFAULT_CAPACITY];
        size = 0;
    }


    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index != -1) {
            for(int i = index + 1; i < size; i++) {
                   elementData[i - 1] = elementData[i];
            }
            size--;
            return true;
        } else {
            return false;
        }
    }
    @Override
    public Object[] toArray() {
        Object[] array = this.elementData;
        return array;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(MyList c) {
            Object[] list = c.toArray();
            for(Object object : list) {
                if(!contains(object)) {
                    return false;
                }
            }
            return true;
        }

// возвращает индекс в данной строке первого вхождения указанного символа или -1, если символ не встречается.
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;

    }
    private class IteratorImpl implements Iterator<Object> {

        int cursor;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public Object next() {
            int i = cursor;
            if(i >= size) {
                throw new NoSuchElementException();
            }
            Object[] elementData = MyListImpl.this.elementData;
            if(i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            return elementData[lastRet = i];
        }

        @Override
        public void remove() {
            if(lastRet < 0) {
                throw new IllegalStateException();
            }
            MyListImpl.this.remove(elementData[lastRet]);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorImpl();
    }


    private class ListIteratorImpl extends IteratorImpl implements ListIterator {

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public Object previous() {
            int i = cursor - 1;
            if(i < 0) {
                throw new NoSuchElementException();
            }
            Object[] elementData = MyListImpl.this.elementData;
            if(i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i;
            return elementData[i];
        }

        @Override
        public void set(Object e) {
            remove();
            MyListImpl.this.add(e);
        }

        @Override
        public void remove() {
            super.remove();
        }
    }

    @Override
    public ListIterator listIterator() {
        return new ListIteratorImpl();
    }
}
