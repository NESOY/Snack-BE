package com.snack.news.dto;


import com.snack.news.exception.NewsBadRequestException;
import org.junit.Test;

import java.time.LocalDateTime;

public class PeriodTest {

	@Test
	public void 두_날짜가_한주_내에_포함되고_같은_달이고_월요일로_시작한다() {
		LocalDateTime startOfMonday = LocalDateTime.of(2019, 7, 29, 0, 0);
		LocalDateTime endOfWednesday = LocalDateTime.of(2019, 7, 31, 0, 0);

		new Period(startOfMonday, endOfWednesday).validationCheck();
	}

	@Test
	public void 두_날짜가_한주_내에_포함되고_같은_달이고_일요일로_끝난다() {
		LocalDateTime startOfWednesday = LocalDateTime.of(2019, 8, 1, 0, 0);
		LocalDateTime endOfSunday = LocalDateTime.of(2019, 8, 4, 0, 0);

		new Period(startOfWednesday, endOfSunday).validationCheck();
	}

	@Test(expected = NewsBadRequestException.class)
	public void 두_날짜가_한주_내에_포함되고_같은_달이지만_월요일로_시작하지_않고_일요일로_끝나지_않으면_예외가_발생한다() {
		LocalDateTime start = LocalDateTime.of(2019, 8, 1, 0, 0);
		LocalDateTime endOfSaturday = LocalDateTime.of(2019, 8, 3, 0, 0);

		new Period(start, endOfSaturday).validationCheck();
	}

	@Test(expected = NewsBadRequestException.class)
	public void 두_날짜가_한주_내에_포함되지만_달이_다르면_예외가_발생한다() {
		LocalDateTime startOfMonday = LocalDateTime.of(2019, 7, 29, 0, 0);
		LocalDateTime endOfSunday = LocalDateTime.of(2019, 8, 4, 0, 0);

		new Period(startOfMonday, endOfSunday).validationCheck();
	}

	@Test(expected = NewsBadRequestException.class)
	public void 두_날짜의_차가_일주일이_넘으면_예외가_발생한다() {
		LocalDateTime startOfMonday = LocalDateTime.of(2019, 7, 29, 0, 0);
		LocalDateTime startOfNextMonday = LocalDateTime.of(2019, 8, 5, 0, 0);

		new Period(startOfMonday, startOfNextMonday).validationCheck();
	}

	@Test(expected = NewsBadRequestException.class)
	public void 날짜_값이_입력되지_않았다면_예외가_발생한다() {
		new Period(null, null).validationCheck();
	}
}
