package com.food.foodItemservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.food.foodItemservice.dto.FoodDto;
import com.food.foodItemservice.dto.FoodImageDto;
import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.Food;
import com.food.foodItemservice.entity.FoodItem;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.feign.RestaurantProxyService;
import com.food.foodItemservice.repository.FoodItemRepository;
import com.food.foodItemservice.repository.FoodRepository;
import com.food.foodItemservice.utils.FoodItemServiceMapping;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemServiceImpl implements FoodItemService {

	private final FoodItemRepository foodItemRepository;
	private final FoodRepository foodRepository;
	private final RestaurantProxyService restaurantProxyService;

	@Override
	@Transactional
	@CircuitBreaker(fallbackMethod = "addFoodItemFallback", name = "fallBackForAddFoodItem")
	public FoodItemDto addFoodItem(FoodItemDto foodItemDto) throws ResourceNotFoundException {
		Boolean restaurantExist = restaurantProxyService.restaurantExistOrNotById(foodItemDto.getRestaurantId())
				.getBody();

		if (!restaurantExist)
			throw new ResourceNotFoundException("Restaurant not found having id: " + foodItemDto.getRestaurantId());
		Food food = foodRepository
				.findFoodByFoodNameAndMealType(foodItemDto.getFood().getFoodName(), foodItemDto.getFood().getMealType())
				.orElse(null);
		FoodItem foodItem = FoodItemServiceMapping.mapFoodItemDtoToFoodItem(foodItemDto);
		foodItem.setFood(food);
		if (food == null)
			foodItem.setFood(FoodItemServiceMapping.mapFoodDtoToFood(foodItemDto.getFood()));
		foodItemRepository.save(foodItem);
		return FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem);
	}

	public FoodItemDto addFoodItemFallback(FoodItemDto foodItemDto, RuntimeException exception)
			throws ResourceNotFoundException {
		FoodDto defaultFood = FoodDto.builder().cuisine("North").foodName("Samosa").id(0L).mealType("Vegetarian")
				.build();
		FoodImageDto defaultFoodImageDto = FoodImageDto.builder().id(0L).image("image").imageName("samosa image")
				.build();
		return FoodItemDto.builder().id(0L).food(defaultFood).image(defaultFoodImageDto)
				.description("Description for samosa").price(0).restaurantId(0L).build();
	}

	@Override
	@Transactional
	@CircuitBreaker(fallbackMethod = "addMultipleFoodItemsFallback", name = "fallBackForAddMultipleFoodItems")
	public List<FoodItemDto> addMultipleFoodItems(List<FoodItemDto> foodItemDtos) throws ResourceNotFoundException {
		if (foodItemDtos.size() == 0)
			throw new ResourceNotFoundException("Add Item First");
		Boolean restaurantExist = restaurantProxyService.restaurantExistOrNotById(foodItemDtos.get(0).getRestaurantId())
				.getBody();
		if (!restaurantExist)
			throw new ResourceNotFoundException(
					"Restaurant not found having id: " + foodItemDtos.get(0).getRestaurantId());

		List<FoodItem> foodItems = foodItemDtos.stream()
				.map(foodItemDto -> FoodItemServiceMapping.mapFoodItemDtoToFoodItem(foodItemDto))
				.collect(Collectors.toList());
		for (FoodItem foodItem : foodItems) {
			Food food = foodRepository
					.findFoodByFoodNameAndMealType(foodItem.getFood().getFoodName(), foodItem.getFood().getMealType())
					.orElse(null);
			if (food != null) {
				foodItem.setFood(food);
			}
		}
		return foodItemRepository.saveAll(foodItems).stream()
				.map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());

	}

	public List<FoodItemDto> addMultipleFoodItemsFallback(List<FoodItemDto> foodItemDtos, RuntimeException exception)
			throws ResourceNotFoundException {
		FoodDto defaultFood = FoodDto.builder().cuisine("North").foodName("Samosa").id(0L).mealType("Vegetarian")
				.build();
		FoodImageDto defaultFoodImageDto = FoodImageDto.builder().id(0L).image("image").imageName("samosa image")
				.build();
		FoodItemDto defaultFoodItemDto = FoodItemDto.builder().id(0L).food(defaultFood).image(defaultFoodImageDto)
				.description("Description for samosa").price(0).restaurantId(0L).build();
		List<FoodItemDto> defaultFoodItemDtoList = new ArrayList<FoodItemDto>();

		defaultFoodItemDtoList.add(defaultFoodItemDto);
		return defaultFoodItemDtoList;
	}

	@Override
	public FoodItemDto getSpecificFoodItemById(Long id) throws ResourceNotFoundException {
		FoodItem foodItem = foodItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FoodItem not found"));
		return FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem);
	}

	@Override
	public FoodItemDto updateFoodItem(FoodItemDto foodItemDto) throws ResourceNotFoundException {
		if (foodItemDto.getId() == null)
			throw new ResourceNotFoundException("Id can not be null");
		FoodItem foodItem = foodItemRepository.findById(foodItemDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Food Item not found"));
		foodItem.setDescription(foodItemDto.getDescription());
		foodItem.setPrice(foodItemDto.getPrice());
		return FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItemRepository.save(foodItem));
	}

	@Override
	public String deleteFoodItemById(Long id) {
		foodItemRepository.deleteById(id);
		return "FoodItem deleted successfully";
	}

	@Override
	public List<FoodItemDto> getAllByRestaurantId(Long restaurantId) throws ResourceNotFoundException {
		List<FoodItem> foodItems = foodItemRepository.findAllByRestaurantId(restaurantId);
		if (foodItems.size() == 0) {
			throw new ResourceNotFoundException("There are no menu items in this restaurant");
		}
		List<FoodItemDto> foodItemDtos = foodItems.stream()
				.map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());
		return foodItemDtos;
	}

	@Override
	public Boolean foodItemExistOrNot(Long id) {
		Optional<FoodItem> foodItem = foodItemRepository.findById(id);
		return foodItem.isPresent();
	}

	@Override
	public Boolean multipleFoodItemExistOrNot(List<Long> ids) {
		List<FoodItem> foodItems = foodItemRepository.findAllById(ids);
		return foodItems.size() == ids.size();
	}

	@Override
	public List<FoodItemDto> getAllFoodItems() throws ResourceNotFoundException {
		List<FoodItem> foodItems = foodItemRepository.findAll();
		if (foodItems.size() == 0) {
			throw new ResourceNotFoundException("There are no food items in the database.");
		}
		return foodItems.stream().map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());
	}

	@Override
	public List<FoodItemDto> getAllByRestaurantIds(List<Long> ids) throws ResourceNotFoundException {
		List<FoodItem> foodItems = foodItemRepository.findAllByRestaurantIdIn(ids);
		if (foodItems.size() == 0) {
			throw new ResourceNotFoundException("There are no food items in the database.");
		}
		return foodItems.stream().map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());
	}

	@Override
	public FoodItemDto getFoodItemById(Long id) throws ResourceNotFoundException {
		FoodItem foodItem = foodItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food Item not found having id: " + id));
		return FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem);

	}

	@Override
	public List<FoodItemDto> getMultipleFoodItemByIds(List<Long> ids) {
		return foodItemRepository.findAllById(ids).stream()
				.map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());
	}
}
