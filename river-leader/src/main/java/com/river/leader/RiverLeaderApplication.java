package com.river.leader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan(basePackages = {"com.river.leader.modular.system.dao*"})
@SpringBootApplication
public class RiverLeaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiverLeaderApplication.class, args);
	}
}
