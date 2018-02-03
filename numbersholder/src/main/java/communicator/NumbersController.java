package communicator;

import holder.NumbersHolder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class NumbersController {

    @RequestMapping("/save")
    public String saveNumber(@RequestParam String number, @RequestParam String ani){
        NumbersHolder.putNumber(number, ani);
        return "saved " + number + " : " + ani;
    }

    @RequestMapping("/contains")
    public String isInside(@RequestParam String number, @RequestParam String ani) {
        if (NumbersHolder.contains(number, ani)) {
            return "yes";
        } else {
            return "no";
        }
    }

    @RequestMapping("/")
    public String home(){
        return "holder is waiting";
    }

    @Scheduled(fixedDelay = 60000)
    public void resetNumbers(){
        NumbersHolder.clear();
    }
}
