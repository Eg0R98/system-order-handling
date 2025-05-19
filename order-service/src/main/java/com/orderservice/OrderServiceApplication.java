package com.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения order-service.
 * <p>
 * Является точкой входа для запуска Spring Boot-приложения.
 * Аннотация {@link SpringBootApplication} объединяет:
 * <ul>
 *     <li>{@code @Configuration} — для объявления конфигурационного класса</li>
 *     <li>{@code @EnableAutoConfiguration} — для автоматической настройки Spring-контекста</li>
 *     <li>{@code @ComponentScan} — для сканирования компонентов в текущем пакете и подпакетах</li>
 * </ul>
 */
@SpringBootApplication
public class OrderServiceApplication {

	/**
	 * Точка входа в приложение.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
