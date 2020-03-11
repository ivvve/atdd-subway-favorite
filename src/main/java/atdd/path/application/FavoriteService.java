package atdd.path.application;

import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.FavoriteRouteRequestView;
import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.domain.FavoriteRoute;
import atdd.path.domain.FavoriteStation;
import atdd.path.repository.FavoriteRouteRepository;
import atdd.path.repository.FavoriteStationRepository;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteStationRepository favoriteStationRepository;
    private final FavoriteRouteRepository favoriteRouteRepository;

    public FavoriteService(FavoriteStationRepository favoriteStationRepository, FavoriteRouteRepository favoriteRouteRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
        this.favoriteRouteRepository = favoriteRouteRepository;
    }

    public FavoriteResponseView createStationFavorite(Long stationId, User user) {
        FavoriteStation savedFavorite = favoriteStationRepository.save(FavoriteStation.builder()
                .userId(user.getId())
                .stationId(stationId)
                .build());

        return FavoriteResponseView.of(savedFavorite);
    }

    public List<FavoriteResponseView> findFavoriteStation(User user) {
        List<FavoriteStation> favorites = favoriteStationRepository.findAllByUserId(user.getId());

        return FavoriteResponseView.listOf(favorites);
    }

    @Transactional
    public void deleteFavoriteStation(User user, Long id) {
        favoriteStationRepository.deleteByIdAndUserId(id, user.getId());
    }

    public FavoriteRouteResponseView createRouteFavorite(FavoriteRouteRequestView view, User user) {
        view.validate();

        FavoriteRoute savedFavorite = favoriteRouteRepository.save(FavoriteRoute.builder()
                .userId(user.getId())
                .sourceStationId(view.getSourceStationId())
                .targetStationId(view.getTargetStationId())
                .build());
        return FavoriteRouteResponseView.of(savedFavorite);
    }

    public List<FavoriteRouteResponseView> findFavoriteRoute(User user) {
        List<FavoriteRoute> favorites = favoriteRouteRepository.findAllByUserId(user.getId());
        return FavoriteRouteResponseView.listOf(favorites);
    }

    public void deleteFavoriteRoute(Long id, User user) {
        favoriteRouteRepository.deleteByIdAndUserId(id, user.getId());
    }
}