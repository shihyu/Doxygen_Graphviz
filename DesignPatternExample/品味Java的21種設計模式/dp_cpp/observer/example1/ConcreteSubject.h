#pragma once

#include "Subject.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example1
				{

					///
					/// <summary> * �����Ŀ����󣬸�����й�״̬���뵽��Ӧ�Ĺ۲��߶���
					/// * �����Լ�״̬�����ı�ʱ��֪ͨ�����۲��� </summary>
					/// 
					class ConcreteSubject : public Subject
					{
					///	
					///	 <summary> * ʾ�⣬Ŀ������״̬ </summary>
					///	 
					private:
						std::string subjectState;
					public:
						virtual std::string getSubjectState();
						virtual void setSubjectState(std::string subjectState);
					};


				}
			}
		}
	}
}