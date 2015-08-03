(ns white-ink.state
  (:require [white-ink.utils.state :refer [make-squuid]]))

(def draft
  "It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning? It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning?")

(def session-2
  "I am session number 2, session number 2. Session TWO TWO TWO.")

(defn- notes-gen
  ([n draft-text] (notes-gen n draft-text ""))
  ([n draft-text infix]
   (let [draft-idxs (sort (repeatedly n #(rand-int (count draft-text))))]
     (vec (map #(hash-map :id (make-squuid)
                          :text (str "note " infix " " %)
                          :draft-index %2)
               (range 1 (inc n))
               draft-idxs)))))

(def mock-notes-first-session (notes-gen 5 draft "first session"))
(def mock-notes-second-session (notes-gen 5 session-2 "second session"))

;; define your app data so that it doesn't get over-written on reload
(def app-state (atom {:user            {:settings {:text-grain false}
                                        ;; metrics would be individual stats about a user calculated over time
                                        :metrics  {:avg-typing-speed 200}}
                      :searching?      false
                      :text-fade-delay 45000
                      :speed->opacity  1
                      :current-draft   {:current-session {:current-insert {:start-idx 3080 ;where insert started in draft
                                                                           :text      "Hi I'm test data, test data, test dada, test dah" ; insert text
                                                                           :removed?  0} ; number of characters removed - if any
                                                          :inserts        [{:start-idx 0 ;where insert started in draft
                                                                            :text      "First instert in current-session" ; insert text
                                                                            :removed?  0}
                                                                           {:start-idx 0 ;where insert started in draft
                                                                            :text      "second instert in current-session" ; insert text
                                                                            :removed?  0}
                                                                           {:start-idx 0 ;where insert started in draft
                                                                            :text      "third instert in current-session" ; insert text
                                                                            :removed?  0}]
                                                          :notes          (notes-gen 3 draft "from session")}
                                        :sessions        [{:inserts [{:start-idx 0
                                                                      :text      draft
                                                                      :removed?  0}
                                                                     {:start-idx 2080 ;where insert started in draft
                                                                      :text      "Hi I'm test data, test data, test dada, test dah" ; insert text
                                                                      :removed?  0}]
                                                           :notes   mock-notes-first-session}
                                                          {:inserts [{:start-idx 5
                                                                      :text      session-2
                                                                      :removed?  0}]
                                                           :notes   mock-notes-second-session}]}
                      :drafts          [; this would contain "other" drafts, i.e. entirely different writing projects
                                        ]}))



