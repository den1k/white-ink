(ns white-ink.state
  (:require [white-ink.utils.state :refer [make-squuid]]))

(def draft
  "It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning? It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning?")

(def session-2
  "I am session number 2, session number 2. Session TWO TWO TWO. This truly is the second session. I swear to you, charles choplin. This can go on forever. An ever and ever, being session # 2.")

(defn- notes-gen
  ([n draft-text] (notes-gen n draft-text ""))
  ([n draft-text infix]
   (let [draft-idxs (sort (repeatedly n #(rand-int (count draft-text))))]
     (vec (map (fn [count idx]
                 (hash-map :id (make-squuid)
                           :text (str "note-" infix "-idx" #_idx #_count)
                           :rel-idx idx))
               (range 1 (inc n))
               draft-idxs)))))

(def mock-notes-first-session (notes-gen 5 draft "first session"))
(def mock-notes-second-session (notes-gen 5 session-2 "second session"))

(def inserts-text
  {:current "I'm the current insert, the one and only current insert bro."
   :first   "First instert in current-session"
   :second  "second instert in current-session"
   :third   "third instert in current-session"})

(def ses-ins-text
  {:first  {:first "I'm the very first insert of the very first session. The first insert of all inserts. Basically the beginning of all writing."}
   :second {:first  "The first inserts in the second session, hi!"
            :second "The second and in the second session. Pleasure to meet you."}})

;; define your app data so that it doesn't get over-written on reload
(def app-state (atom {:user              {:settings {:text-grain false}
                                          ;; metrics would be individual stats about a user calculated over time
                                          :metrics  {:avg-typing-speed 200}}
                      :quick-settings    {:show? false
                                          :items {:current-document?   true
                                                  :documents?          false
                                                  :keyboard-shortcuts? false
                                                  :settings?           false}}
                      :searching?        false
                      :text-fade-delay   45000
                      :review-scroll-top 2000
                      :speed->opacity    1
                      :current-draft     {:current-session {:current-insert {:start-idx 20 ;where insert started in draft
                                                                             :text      (:current inserts-text) ; insert text
                                                                             :removed?  0
                                                                             :notes     (notes-gen 2 (:current inserts-text) "cur ses cur ins")} ; number of characters removed - if any
                                                            ;; inserts are not visible until the end of the session, when it is added to `sessions`
                                                            :inserts        [{:start-idx 0 ;where insert started in draft
                                                                              :text      (:first inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:first inserts-text) "cur ses 1st ins")}
                                                                             {:start-idx 0 ;where insert started in draft
                                                                              :text      (:second inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:second inserts-text) "cur ses 2nd ins")}
                                                                             {:start-idx 0 ;where insert started in draft
                                                                              :text      (:third inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:third inserts-text) "cur ses 3rd ins")}]}
                                          :sessions        [
                                                            ; first session
                                                            [
                                                             ; first insert
                                                             {:start-idx                                  0
                                                              :text     #_(-> ses-ins-text :first :first) "0123456789"
                                                              :removed?                                   0
                                                              :notes                                      [{:text    "note 0 0" ;; from session: insert 0, note 0
                                                                                                            :rel-idx 2}
                                                                                                           {:text    "note 0 1"

                                                                                                            :rel-idx 4}]}]
                                                            ; second session
                                                            [
                                                             ; first insert
                                                             {:start-idx                                   5
                                                              :text     #_(-> ses-ins-text :second :first) "abcdefghijklm"
                                                              :removed?                                    5
                                                              :notes                                       [{:text    "note 0 0"
                                                                                                             :rel-idx 1}]}
                                                             ; second insert
                                                             {:start-idx                                    3
                                                              :text     #_(-> ses-ins-text :second :second) "NOPVWQRSTXYZ"
                                                              :removed?                                     0
                                                              :notes                                        [{:text    "note 1 0"
                                                                                                              :rel-idx 3}]}]]}
                      :drafts            [; this would contain "other" drafts, i.e. entirely different writing projects
                                          ]}))



