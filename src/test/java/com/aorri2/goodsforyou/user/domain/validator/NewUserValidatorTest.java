package com.aorri2.goodsforyou.user.domain.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.aorri2.goodsforyou.user.application.NewUserFinder;
import com.aorri2.goodsforyou.user.application.NewUserValidator;
import com.aorri2.goodsforyou.user.application.command.CreateUserCommand;
import com.aorri2.goodsforyou.user.domain.LoginUserPolicy;
import com.aorri2.goodsforyou.user.domain.NewUser;
import com.aorri2.goodsforyou.user.domain.UserFinder;
import com.aorri2.goodsforyou.user.domain.UserPolicy;
import com.aorri2.goodsforyou.user.domain.UserRepositoryPort;
import com.aorri2.goodsforyou.user.domain.policy.LoginUserEmailPolicy;
import com.aorri2.goodsforyou.user.domain.policy.LoginUserPasswordPolicy;
import com.aorri2.goodsforyou.user.domain.policy.NewUserEmailPolicy;
import com.aorri2.goodsforyou.user.domain.policy.NewUserNamePolicy;
import com.aorri2.goodsforyou.user.domain.policy.NewUserPasswordPolicy;
import com.aorri2.goodsforyou.user.infrastructure.inmemory.MemoryUserRepositoryAdapter;

@DisplayName("NewUserValidator 클래스")
class NewUserValidatorTest {

	List<UserPolicy> validityPolicyList;

	NewUserValidator validator;

	UserFinder userFinder;
	UserRepositoryPort userRepositoryPort;
	List<LoginUserPolicy> loginUserPolicyList;

	@BeforeEach
	void setUp() {

		userRepositoryPort = new MemoryUserRepositoryAdapter();
		userFinder = new NewUserFinder(userRepositoryPort);
		validityPolicyList = List.of(new NewUserEmailPolicy(userFinder), new NewUserNamePolicy(userFinder),
			new NewUserPasswordPolicy());
		loginUserPolicyList = List.of(new LoginUserEmailPolicy(userFinder), new LoginUserPasswordPolicy(userFinder));
		validator = new NewUserValidator(validityPolicyList, loginUserPolicyList);
	}

	@Nested
	@DisplayName("checkUserValidity메소드는")
	class Describe_checkUserValidity {

		@Nested
		@DisplayName("만약 이메일과 이름이 중복된다면 ")
		class Context_with_emailAndName {
			@BeforeEach
			void setUp() {
				userRepositoryPort.save(new NewUser("wook@naver.com", "wook", "123123121"));
			}

			@Test
			@DisplayName("RuntimeException을 발생시킨다.")
			void it_execute_policySize() {
				CreateUserCommand user = new CreateUserCommand("wook@naver.com", "wook", "123123121");

				assertThatThrownBy(() -> validator.checkUserValidity(user)).isInstanceOf(RuntimeException.class)
					.hasMessage("이미 존재하는 이메일 입니다.");
			}
		}
	}
}