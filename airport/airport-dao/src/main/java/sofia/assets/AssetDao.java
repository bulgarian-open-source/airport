package sofia.assets;

import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.keygen.IKeyNumber;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAsset}.
 *
 * @author Developers
 *
 */
@EntityType(Asset.class)
public class AssetDao extends CommonEntityDao<Asset> implements IAsset {
    
    public static final String DEFAULT_ASSET_NUMBER = "NEXT NUMBER WILL BE GENERATED UPON SAVE.";
    public static final String ERR_FAILED_SAVE = "Deliberate save exception.";

    @Inject
    public AssetDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
        public Asset new_() {
            // TODO Auto-generated method stub
            final Asset asset =  super.new_();
            asset.setNumber(DEFAULT_ASSET_NUMBER);
            return asset;
        }
    
    @Override
    @SessionRequired
    public Asset save(final Asset asset) {
        // TODO implement a solution for a failed transaction where ID was already assigned
        if (!asset.isPersisted()) {
            final IKeyNumber coKeyNumber = co(KeyNumber.class);
            String nextNumber = coKeyNumber.nextNumber("ASSET_NUMBER").toString();
            String toAdd = IntStream.range(0, 6 - nextNumber.length())
                    .mapToObj(x -> "0")
                    .collect(Collectors.joining());
            asset.setNumber(toAdd.concat(nextNumber));
        }
        return super.save(asset);
    }
    
    @SessionRequired
    public Asset saveWithError(final Asset asset) {
        save(asset);
        throw Result.failure(ERR_FAILED_SAVE);
    }

    @Override
    @SessionRequired
    public int batchDelete(final Collection<Long> entitiesIds) {
        return defaultBatchDelete(entitiesIds);
    }

    @Override
    @SessionRequired
    public int batchDelete(final List<Asset> entities) {
        return defaultBatchDelete(entities);
    }

    @Override
    protected IFetchProvider<Asset> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAsset.FETCH_PROVIDER. Then remove the line after.
      return FETCH_PROVIDER;
    }}