package kaphein.template.cmstemplatebatis.persistence.batis;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import kaphein.template.cmstemplatebatis.persistence.Pagination;
import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;

/**
 * 페이징 쿼리를 구현하기 위한 SQL 쿼리에 대한 인터셉터
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class,
        Object.class,
        RowBounds.class,
        ResultHandler.class
    }),
    @Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class,
        Object.class,
        RowBounds.class,
        ResultHandler.class,
        CacheKey.class,
        BoundSql.class
    })
})
public class PaginationInterceptor
    implements Interceptor
{
    /**
     * 쿼리 파라미터에서 페이지네이션 파라미터가 발견되는 경우 페이징 쿼리를 수행한다.
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        Object invocationResult = null;

        if(!"query".equals(invocation.getMethod().getName()))
        {
            invocationResult = invocation.proceed();
        }
        else
        {
            final Object[] invocationArgs = invocation.getArgs();
            final Object parameter = invocationArgs[ARG_INDEX_OF_PARAMETER];

            final PaginationParameter paginationParameter = findPagingQueryParameterIn(parameter);
            if(null == paginationParameter)
            {
                invocationResult = invocation.proceed();
            }
            else
            {
                final Map<String, Object> paginationParam = paginationParameter.toMap();
                paginationParam.put("take", paginationParameter.getItemCountPerPage());
                paginationParam.put(
                    "skip",
                    paginationParameter.getItemCountPerPage() * paginationParameter.getCurrentPageIndex()
                );

                final MappedStatement ms = (MappedStatement) invocationArgs[ARG_INDEX_OF_MAPPED_STATEMENT];
                paginationParam.put("subQuery", ms.getBoundSql(parameter).getSql());

                final long itemTotalCount = findPageItemTotalCount(ms, paginationParam, invocation);
                final List<Object> items = findByPage(ms, paginationParam, invocation);

                final Pagination pagination = new Pagination(itemTotalCount,
                    paginationParameter.getItemCountPerPage(),
                    paginationParameter.getCurrentPageIndex(),
                    paginationParameter.getDisplayedPageCount()
                );

                invocationResult = new PaginationResult<>(items, pagination);
            }
        }

        return invocationResult;
    }

    /**
     * MyBatis invocadtion에서 {@link PaginationParameter}를 찾는다.
     *
     * @param parameter invocation에 지정된 파라미터
     * @return {@link PaginationParameter}의 인스턴스. 없으면 `null`.
     */
    @SuppressWarnings("unchecked")
    private PaginationParameter findPagingQueryParameterIn(Object parameter)
    {
        PaginationParameter paginationParameter = null;

        Object candidate = parameter;
        if(parameter instanceof Map)
        {
            Map<Object, Object> m = (Map<Object, Object>) parameter;
            if(m.containsKey(PAGINATION_PARAMETER_NAME))
            {
                candidate = m.get(PAGINATION_PARAMETER_NAME);
            }
        }

        if(candidate instanceof PaginationParameter)
        {
            paginationParameter = (PaginationParameter) candidate;
        }

        return paginationParameter;
    }

    /**
     * {@link PagerMapper#findPageItemTotalCount}를 실행한다.
     *
     * @param originalMs
     * @param paginationParam
     * @param invocation
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private long findPageItemTotalCount(
        MappedStatement originalMs,
        Map<String, Object> paginationParam,
        Invocation invocation
    ) throws InvocationTargetException, IllegalAccessException
    {
        final org.apache.ibatis.session.Configuration batisConfig = originalMs.getConfiguration();
        final MappedStatement ms = batisConfig.getMappedStatement(String.format(
            "%s.%s",
            PAGINATION_QUERY_ID_PREFIX,
            "findPageItemTotalCount"
        ));
        final Object[] invocationArgs = Arrays.copyOf(invocation.getArgs(), invocation.getArgs().length);
        invocationArgs[0] = ms;
        invocationArgs[1] = paginationParam;

        final Object invocationResult = invocation.getMethod().invoke(invocation.getTarget(), invocationArgs);
        long itemTotalCount = 0;
        if(null != invocationResult)
        {
            if(invocationResult instanceof List)
            {
                itemTotalCount = (
                    (List<Long>) invocation.getMethod()
                        .invoke(invocation.getTarget(), invocationArgs)
                ).stream().findFirst().orElse(0L);
            }
            else if(invocationResult instanceof Number)
            {
                itemTotalCount = ((Number) invocationResult).longValue();
            }
            else
            {
                throw new ClassCastException();
            }
        }

        return itemTotalCount;
    }

    /**
     * {@link PagerMapper#findByPage}를 실행한다.
     *
     * @param originalMs
     * @param paginationParam
     * @param invocation
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private List<Object> findByPage(
        MappedStatement originalMs,
        Map<String, Object> paginationParam,
        Invocation invocation
    ) throws InvocationTargetException, IllegalAccessException
    {
        final org.apache.ibatis.session.Configuration batisConfig = originalMs.getConfiguration();
        final MappedStatement findByPageMs = batisConfig.getMappedStatement(String.format(
            "%s.%s",
            PAGINATION_QUERY_ID_PREFIX,
            "findByPage"
        ));
        final Object[] findByPageInvocationArgs = Arrays.copyOf(invocation.getArgs(), invocation.getArgs().length);
        findByPageInvocationArgs[0] = findByPageMs;
        findByPageInvocationArgs[1] = paginationParam;

        final Object invocationResult = invocation.getMethod().invoke(invocation.getTarget(), findByPageInvocationArgs);
        List<Object> result = Collections.emptyList();
        if(null != invocationResult)
        {
            if(invocationResult instanceof List)
            {
                result = (List<Object>) invocationResult;
            }
            else
            {
                throw new ClassCastException();
            }
        }

        return result;
    }

    private static final String PAGINATION_PARAMETER_NAME = "pagination";

    private static final String PAGINATION_QUERY_ID_PREFIX = PagerMapper.class.getName();

    private static final int ARG_INDEX_OF_MAPPED_STATEMENT = 0;

    private static final int ARG_INDEX_OF_PARAMETER = 1;
}
