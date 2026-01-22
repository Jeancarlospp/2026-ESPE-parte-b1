package es.upm.grise.profundizacion.subscriptionService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.upm.grise.profundizacion.exceptions.ExistingUserException;
import es.upm.grise.profundizacion.exceptions.LocalUserDoesNotHaveNullEmailException;
import es.upm.grise.profundizacion.exceptions.NullUserException;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	@BeforeEach
	public void setUp() {
		subscriptionService = new SubscriptionService();
	}
	
	
	@Test
	public void testAddSubscriberWithNullUserThrowsException() {
		assertThrows(NullUserException.class, () -> {
			subscriptionService.addSubscriber(null);
		});
	}
	
	@Test
	public void testAddValidUserWithDoNotDeliverSuccessfully() throws Exception {
		User user = new User();
		user.setDelivery(Delivery.DO_NOT_DELIVER);
		user.setEmail("test@example.com");
		
		subscriptionService.addSubscriber(user);
		
		assertTrue(subscriptionService.getSubscribers().contains(user));
		assertEquals(1, subscriptionService.getSubscribers().size());
	}
	
	@Test
	public void testAddValidUserWithLocalDeliveryAndNullEmailSuccessfully() throws Exception {
		User user = new User();
		user.setDelivery(Delivery.LOCAL);
		user.setEmail(null);
		
		subscriptionService.addSubscriber(user);
		
		assertTrue(subscriptionService.getSubscribers().contains(user));
	}
	
	@Test
	public void testAddExistingUserThrowsException() throws Exception {
		User user = new User();
		user.setDelivery(Delivery.DO_NOT_DELIVER);
		user.setEmail("usuario@mail.com");
		
		subscriptionService.addSubscriber(user);
		
		assertThrows(ExistingUserException.class, () -> {
			subscriptionService.addSubscriber(user);
		});
	}
	
	@Test
	public void testAddUserWithLocalDeliveryAndNonNullEmailThrowsException() {
		User user = new User();
		user.setDelivery(Delivery.LOCAL);
		user.setEmail("correo@dominio.com");
		
		assertThrows(LocalUserDoesNotHaveNullEmailException.class, () -> {
			subscriptionService.addSubscriber(user);
		});
	}
	
	@Test
	public void testAddMultipleUsersSuccessfully() throws Exception {
		User user1 = new User();
		user1.setDelivery(Delivery.DO_NOT_DELIVER);
		user1.setEmail("user1@mail.com");
		
		User user2 = new User();
		user2.setDelivery(Delivery.LOCAL);
		user2.setEmail(null);
		
		User user3 = new User();
		user3.setDelivery(Delivery.DO_NOT_DELIVER);
		user3.setEmail("user3@mail.com");
		
		subscriptionService.addSubscriber(user1);
		subscriptionService.addSubscriber(user2);
		subscriptionService.addSubscriber(user3);
		
		assertEquals(3, subscriptionService.getSubscribers().size());
	}
	
	@Test
	public void testServiceStartsWithNoSubscribers() {
		assertTrue(subscriptionService.getSubscribers().isEmpty());
	}
	
	@Test
	public void testUserNotAddedWhenExceptionThrown() {
		User user = new User();
		user.setDelivery(Delivery.LOCAL);
		user.setEmail("email@test.com");
		
		try {
			subscriptionService.addSubscriber(user);
			fail("Deberia haber lanzado LocalUserDoesNotHaveNullEmailException");
		} catch (LocalUserDoesNotHaveNullEmailException e) {
			
		} catch (Exception e) {
			fail("Se lanzo una excepcion inesperada");
		}
		
		assertFalse(subscriptionService.getSubscribers().contains(user));
		assertEquals(0, subscriptionService.getSubscribers().size());
	}
	
	@Test
	public void testAddUserWithDoNotDeliverAndNullEmailIsValid() throws Exception {
		User user = new User();
		user.setDelivery(Delivery.DO_NOT_DELIVER);
		user.setEmail(null);
		
		subscriptionService.addSubscriber(user);
		
		assertEquals(1, subscriptionService.getSubscribers().size());
	}
}
