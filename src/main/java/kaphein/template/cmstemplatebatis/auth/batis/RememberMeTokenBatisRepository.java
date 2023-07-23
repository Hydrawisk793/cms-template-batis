package kaphein.template.cmstemplatebatis.auth.batis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kaphein.template.cmstemplatebatis.persistence.TransactionService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import kaphein.template.cmstemplatebatis.auth.RememberMeToken;

@Repository
public class RememberMeTokenBatisRepository implements PersistentTokenRepository
{
    // TODO : DEFAULT_TIME_ZONE_ID 상수 따로 빼기
    public static final String DEFAULT_TIME_ZONE_ID = ZoneId.of("Asia/Seoul").getId();

    public RememberMeTokenBatisRepository(
        TransactionService transactionService,
        RememberMeTokenMapper rememberMeTokenMapper
    )
    {
        this.transactionService = Objects.requireNonNull(transactionService);
        this.rememberMeTokenMapper = Objects.requireNonNull(rememberMeTokenMapper);
        setTimeZoneId(DEFAULT_TIME_ZONE_ID);
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token)
    {
        transactionService.doReadCommitted((txStatus) ->
        {
            final RememberMeToken rememberMeToken = new RememberMeToken();
            rememberMeToken.setSeriesId(token.getSeries());
            rememberMeToken.setLoginName(token.getUsername());
            rememberMeToken.setTokenValue(token.getTokenValue());
            rememberMeToken.setLastUsedAt(LocalDateTime.ofInstant(token.getDate().toInstant(), ZoneId.of(timeZoneId)));
            rememberMeTokenMapper.insert(rememberMeToken.toDatabaseTableRow());
        });
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed)
    {
        transactionService.doReadCommitted((txStatus) ->
        {
            final RememberMeToken rememberMeToken = new RememberMeToken();
            rememberMeToken.setSeriesId(series);
            rememberMeToken.setTokenValue(tokenValue);
            rememberMeToken.setLastUsedAt(LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.of(timeZoneId)));
            rememberMeTokenMapper.update(rememberMeToken.toDatabaseTableRow());
        });
    }

    @Override
    public void removeUserTokens(String username)
    {
        transactionService.doReadCommitted((txStatus) ->
        {
            final Map<String, Object> deleteParam = new HashMap<>();
            deleteParam.put("loginName", username);
            rememberMeTokenMapper.delete(deleteParam);
        });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId)
    {
        return transactionService.doReadCommittedWithResult((txStatus) ->
        {
            PersistentRememberMeToken result = null;

            final Map<String, Object> queryParam = new HashMap<>();
            queryParam.put("seriesId", seriesId);

            final Map<String, Object> row = rememberMeTokenMapper.findOneBySeriesId(queryParam);
            if(null != row)
            {
                final RememberMeToken rememberMeToken = new RememberMeToken();
                rememberMeToken.assignDatabaseTableRow(row);

                result = new PersistentRememberMeToken(
                    rememberMeToken.getLoginName(),
                    rememberMeToken.getSeriesId(),
                    rememberMeToken.getTokenValue(),
                    Date.from(rememberMeToken.getLastUsedAt().atZone(ZoneId.of(timeZoneId)).toInstant())
                );
            }

            return result;
        });
    }

    public void setTimeZoneId(String timeZoneId)
    {
        this.timeZoneId = timeZoneId;
    }

    private final TransactionService transactionService;

    private final RememberMeTokenMapper rememberMeTokenMapper;

    private String timeZoneId;
}
