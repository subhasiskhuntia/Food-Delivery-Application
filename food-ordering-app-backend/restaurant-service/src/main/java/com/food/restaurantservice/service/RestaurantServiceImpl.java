package com.food.restaurantservice.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.restaurantservice.dto.RestaurantAddressDto;
import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.exceptions.UserNotFoundException;
import com.food.restaurantservice.feign.ProxyService;
import com.food.restaurantservice.model.Restaurant;
import com.food.restaurantservice.repository.RestaurantAddressRepository;
import com.food.restaurantservice.repository.RestaurantRepository;
import com.food.restaurantservice.utils.AppConstants;
import com.food.restaurantservice.utils.AppUtils;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
	@Autowired
	private final RestaurantRepository restaurantRepository;
	@Autowired
	private final RestaurantAddressRepository restaurantAddressRepository;
	@Autowired
	private final ProxyService proxyService;

	@Override
	@Transactional
	@CircuitBreaker(fallbackMethod = "registerFallback", name = "fallBackForRegister")
	public String register(RestaurantDto restaurantDto) throws RestaurantAlreadyExistsException, UserNotFoundException {

		try {
			Object response = proxyService.getUserByEmail(restaurantDto.getUserEmail());
			String idString = String.valueOf(((Map<String, Object>) response).get("id"));
			Long id = Long.valueOf(idString);

			Restaurant restaurant = restaurantRepository.findRestaurantByUserId(id);
			if (restaurant != null) {
				throw new RestaurantAlreadyExistsException("One user cannot have more than one restaurant");
			}
			Restaurant newlyAddedRestaurant = restaurantRepository
					.save(AppUtils.mapRestaurantDtoToRestaurant(restaurantDto, id));

			newlyAddedRestaurant.getAddress().setRestaurant(newlyAddedRestaurant);
			restaurantAddressRepository.save(newlyAddedRestaurant.getAddress());
			return "Restaurant Saved Successfully";
		} catch (FeignException.NotFound ex) {
			throw new UserNotFoundException("user not found");
		}
	}

	public String registerFallback(RestaurantDto restaurantDto, RuntimeException exception)
			throws RestaurantAlreadyExistsException, UserNotFoundException {
		return "User Service is currently down. Please try again sometime.";
	}

	@Override
	public RestaurantDto findRestaurantById(long id) throws RestaurantNotFoundException {

		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new RestaurantNotFoundException(AppConstants.restaurantNotFoundHavingid(id)));

		return AppUtils.mapRestaurantToRestaurantDto(restaurant, null);
	}

	@Override
	@CircuitBreaker(fallbackMethod = "findRestaurantByEmailFallback", name = "fallBackForFindRestaurantByEmail")
	public RestaurantDto findRestaurantByEmail(String email) throws RestaurantNotFoundException {

		Object response = proxyService.getUserByEmail(email);
		String idString = String.valueOf(((Map<String, Object>) response).get("id"));
		Long id = Long.valueOf(idString);
		System.out.println(id);
		Restaurant restaurant = restaurantRepository.findRestaurantByUserId(id);
		if (restaurant == null) {
			throw new RestaurantNotFoundException(AppConstants.restaurantNotFoundHavingEmail(email));
		}
		return AppUtils.mapRestaurantToRestaurantDto(restaurant, email);
	}

	public RestaurantDto findRestaurantByEmailFallback(String email, RuntimeException exception)
			throws RestaurantNotFoundException {
		RestaurantAddressDto defaultAddressDto = RestaurantAddressDto.builder().id(0).houseNumber("0")
				.locality("Gachibowli").city("Hyderabad").state("Telangana").latitude(0).longitude(0).pincode(123456)
				.build();
		return RestaurantDto.builder().id(0).mobileNumber("0000000000").userEmail("default@gmail.com")
				.address(defaultAddressDto).approvalStatus(false).closedAt("00:00").openedAt("00:00")
				.description("Default description of Restaurant").name("Default Restaurant").image("image").build();
	}

	@Override
	public Boolean restaurantExistOrNotById(long id) {
		return restaurantRepository.findById(id).isPresent();
	}

	@Override
	public List<RestaurantDto> getAllRestaurants() throws RestaurantNotFoundException {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		if (restaurants.size() == 0) {
			throw new RestaurantNotFoundException("No Restaurants Found.");
		}
		return restaurants.stream().map((restaurant) -> AppUtils.mapRestaurantToRestaurantDtoWithoutEmail(restaurant))
				.collect(Collectors.toList());
	}

	@Override
	public String changeApprovalStatus(Long restaurantId) throws RestaurantNotFoundException {
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
				() -> new RestaurantNotFoundException(AppConstants.restaurantNotFoundHavingid(restaurantId)));
		restaurant.setApprovalStatus(!restaurant.isApprovalStatus());
		restaurantRepository.save(restaurant);
		return "Approval status changed successfully";
	}

	@Override
	public String deleteRestaurantById(Long restaurantId) throws RestaurantNotFoundException {
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
				() -> new RestaurantNotFoundException(AppConstants.restaurantNotFoundHavingid(restaurantId)));
		restaurantRepository.deleteById(restaurantId);
		return "Restaurant deleted successfully";
	}

	@Override
	public List<RestaurantDto> getMultipleRestaurantsByIds(List<Long> restaurantIds) {
		return restaurantRepository.findAllById(restaurantIds).stream()
				.map(restaurant -> AppUtils.mapRestaurantToRestaurantDto(restaurant, null))
				.collect(Collectors.toList());
	}

}
