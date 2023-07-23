package kaphein.template.cmstemplatebatis.persistence;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  리스트에 대한 페이지네이션 쿼리 결과
 *
 *  @param <T> 리스트 아이템의 자료형
 */
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class PaginationResult<T> implements List<T>
{
    public PaginationResult(
        List<T> items,
        Pagination pagination
    )
    {
        this.items = Collections.unmodifiableList(items);
        this.pagination = pagination;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<T> getItems()
    {
        return items;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Pagination getPagination()
    {
        return pagination;
    }

    @Override
    public int size()
    {
        return items.size();
    }

    @Override
    public boolean isEmpty()
    {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return items.contains(o);
    }

    @Override
    public Iterator<T> iterator()
    {
        return items.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action)
    {
        items.forEach(action);
    }

    @Override
    public Object[] toArray()
    {
        return items.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a)
    {
        //noinspection SuspiciousToArrayCall
        return items.toArray(a);
    }

    @Override
    public boolean add(T t)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index)
    {
        return items.get(index);
    }

    @Override
    public T set(int index, T element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o)
    {
        return items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return items.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return items.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        return items.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return items.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator()
    {
        return items.spliterator();
    }

    @Override
    public Stream<T> stream()
    {
        return items.stream();
    }

    @Override
    public Stream<T> parallelStream()
    {
        return items.parallelStream();
    }

    /**
     *  조회된 리스트 아이템들
     */
    private final List<T> items;

    /**
     *  페이지네이션 정보
     */
    private final Pagination pagination;
}
