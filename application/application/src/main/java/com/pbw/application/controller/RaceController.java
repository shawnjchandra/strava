package com.pbw.application.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.activityWithEndDate.ActivityEndDateCalculator;
import com.pbw.application.activityWithEndDate.ActivityWithEndDate;
import com.pbw.application.admin.Admin;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.hash.HashService;
import com.pbw.application.race.RaceService;
import com.pbw.application.race.RaceWinner;
import com.pbw.application.user.User;
import com.pbw.application.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Controller
@RequestMapping("/race")
public class RaceController {
    
    @Autowired
    private ActivityService activityService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ActivityWithEndDate activityWithEndDate;


    @Autowired
    private HashService hashService;

    @GetMapping("/")
    public String getRaceListView(
        Model model,
        HttpSession httpSession
    ) throws NoSuchAlgorithmException, IOException{

        
        int id_user = (int)httpSession.getAttribute("id_user");
        String nama = ((User)httpSession.getAttribute("status")).getNama();

        int id_runner = userService.getIdRunnerByIdUsers(id_user);

        CustomResponse<List<Activity>> available = raceService.getAllAvailableRace(id_runner);
        CustomResponse<List<Activity>> joined = raceService.getAllJoinedRace(id_runner);

        if(joined.isSuccess()){
            List<Activity> joined_list = joined.getData();
    
            for(Activity act : joined_list){
                String originalId = String.valueOf(act.getIdActivity());
                String hashedId = hashService.hashIdToInt(originalId);
            
                act.setIdActivity(Integer.valueOf(hashedId));
            }

        }

        model.addAttribute("nama", nama);
        // 
        model.addAttribute("available_obj", available);
        model.addAttribute("available", available.getData());

        model.addAttribute("joined_obj", joined);
        model.addAttribute("joined", joined.getData());

        return "race/racelist";
    }

    @GetMapping("/create")
    @RequiredRole("admin")
    public String getCreateRace(){

        return "race/create";
    }

    @PostMapping("/add")
    public String addRace(
        @RequestParam String judul,
        @RequestParam String tipe_race,
        @RequestParam String createdAt,
        @RequestParam int days,
        @RequestParam int hours,
        @RequestParam int minutes,
        @RequestParam int kuota,
        @RequestParam float jarak,
        @RequestParam(required = false) String description,
        Model model,
        HttpSession httpSession
    ){


        // Hard code dulu
        int id_admin = 1;

        // int id_dmin = (int)httpSession.getAttribute("admin").getId_Admin();

        // hardcode untuk menjadi jam
        hours += days * 24;
        String durasi = String.format("%02d:%02d:%02d", hours, minutes,0);


        Activity act = new Activity();
        act.setJudul(judul);
        act.setTipeRace(tipe_race);
        act.setCreatedAt(LocalDateTime.parse(createdAt + "T00:00:00"));
        act.setDurasi(durasi);
        act.setKuota_max(kuota);
        act.setJarak(jarak);
        act.setDescription(description);

        CustomResponse<Null> res= raceService.addRace(act, id_admin);

        if(res.isSuccess()){

            return "redirect:/admin/index";
        }else{
            return "redirect:/race/create";
        }
        
    }

    @GetMapping("/challenge/{id}")
    // @RequiredRole("admin")
    public String getRaceChallenge(
        @PathVariable("id") String id,
        Model model,
        HttpSession httpSession
        ) throws NumberFormatException, IOException{
        
        

            int id_user = (int)httpSession.getAttribute("id_user");
            int id_activity = Integer.valueOf(hashService.getIdByHashedId(id));

            boolean isAdmin = httpSession.getAttribute("role").equals("admin");

            ActivityWithEndDate race = activityWithEndDate.getSingleActWithEndDate(id_activity);

            CustomResponse<Boolean> isValid = activityService.isRaceEnded(race);

            // untuk cari tipe training yang sama
            String type = race.getActivity().getTipeRace();
            //System.out.println("tipe dari race"+type);
            
        
        // Kalau dia runner aja && belum selesai
        if(!isAdmin && isValid.isSuccess()){
            int id_runner = userService.getIdRunnerByIdUsers(id_user);
            
            // CustomResponse<List<Activity>> listOfActivities = activityService.findTrainingOnlyByIdRunner(id_runner);
            CustomResponse<List<Activity>> listOfActivities = activityService.findTrainingAccordingToType(id_runner,type);

            // Ada error handling di dalam service nya apabila listOfActivities Null / tidak ada
            CustomResponse<List<Activity>> availableActivities = activityService.filterTrainingAccordingDate(listOfActivities.getData(), race);

                    // Untuk cek submission dari user ini
            Participant sumbission = new Participant().userOfThisRace(id_runner, id_activity);
            System.out.println("submission: "+sumbission);

            model.addAttribute("availableActivities", availableActivities);

            // nanti harus di cek kalo udah pernah submit, ga boleh nambah
            model.addAttribute("submission", sumbission);
            model.addAttribute("isValidRace", true);
        }

        
        
        

        List<Integer> participants = raceService.getParticipantsOfSpecificRace(id_activity);

        List<Participant> raceParticipants = new ArrayList<>();

        // TODO: belum tau kalau harus dicek si racenya kosong atau ga
        // Seharusnya ga, karena untuk nge cek racenya harus join, jadi selalu minimal 1
        // Kecuali kalau baru dibikin sama admin sih

        // Bungkus pake customResponse
        CustomResponse<List<Participant>> wrappedRaceParticipants;
        
        if (participants.size()>0) {
            
            for(Integer participant : participants){
                int participant_id = participant.intValue();
    
                Participant prt = new Participant().userOfThisRace(participant_id, id_activity);
    
                raceParticipants.add(prt);
            }

            wrappedRaceParticipants = new CustomResponse<>(true, "Capacity: "+raceParticipants.size(), raceParticipants);
        
        }else{
            wrappedRaceParticipants = new CustomResponse<>(false,  "Capacity: "+raceParticipants.size(), raceParticipants);
        }

        // Kalau udah selesai
        if(!isValid.isSuccess()){
            CustomResponse<RaceWinner> winner = raceService.getWinnerFromRace(id_activity);

            if(!isAdmin){
                int id_runner = userService.getIdRunnerByIdUsers(id_user);
                
                System.out.println("winner "+winner.isSuccess());

                if(winner.isSuccess() && winner.getData().getId_runner() == id_runner){
                    model.addAttribute("isSelf", true);
                }else{
                    model.addAttribute("isSelf", false);

                }
                model.addAttribute("winner",winner);

            }

            model.addAttribute("isValidRace", false);
        }


        model.addAttribute("participants", wrappedRaceParticipants);
        model.addAttribute("numberOfParticipants", wrappedRaceParticipants.getData().size());

        // Buat nambahin submission
        model.addAttribute("id", id);

        model.addAttribute("isAdmin", isAdmin);
        
        model.addAttribute("race", race);

        return "race/challenge";
    }

    @Getter
    @Setter
    class Participant{

        private CustomResponse<Activity> activity;
        private User user;

        protected Participant userOfThisRace(int id_user, int id_activity){
            Participant participant = new Participant();

            User user = userService.getUserByIdRunner(id_user);

            int id_race = raceService.getIdRaceByIdActivity(id_activity);

            CustomResponse<Activity> activity = activityService.getSubmitedActivityOnRace(id_user, id_race);


            //System.out.println("activity submission : "+activity.isSuccess());
            //System.out.println("activity : "+activity.getData());

            participant.setUser(user);
            participant.setActivity(activity);

            return participant;
        }
        
    }

    

    @PostMapping("/join/{id}")
    @ResponseBody
    public ResponseEntity<String> joinRace(
        @PathVariable("id") String id,
        HttpSession httpSession
        ){

        int id_user = (int)httpSession.getAttribute("id_user");
        int id_runner = userService.getIdRunnerByIdUsers(id_user);

        int id_activity = Integer.valueOf(id);

        CustomResponse<Null> result =raceService.joinRace(id_runner,id_activity);
        
        // int id_race = raceService.getIdRaceByIdActivity(id_activity);
        // //System.out.println("id race : "+id_race);

        return ResponseEntity.ok(result.getMessage());
    }

    @PostMapping("/challenge/{id}/selectActivity")
    public String addSubmission(
        @PathVariable("id") String id,
        @RequestParam("activityId") String activityId,
        Model model,
        HttpSession httpSession
    ) throws NumberFormatException, IOException{

        int id_user = (int)httpSession.getAttribute("id_user");
        int id_runner = userService.getIdRunnerByIdUsers(id_user);

        // id activity dari race
        int id_activity_race = Integer.valueOf(hashService.getIdByHashedId(id));

        // id activity dari training yang disubmit
        int id_activity_training = Integer.valueOf(activityId);

        boolean res = raceService.addSubmissionToRace(id_runner, id_activity_race, id_activity_training);

        // if(!res){
        //     return
        // }

        return "redirect:/race/challenge/"+id;
    }

}
