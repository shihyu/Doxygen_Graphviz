#pragma once

#include "Subject.h"
#include <string>

namespace publishSubscribe {

    class BossSubject : public Subject {
    private:
        std::string subjectState;

    public:
        virtual std::string getSubjectState();

        virtual void setSubjectState(std::string subjectState);
    };

}