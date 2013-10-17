#pragma once

#include "Observer.h"
#include "Subject.h"
#include "ConcreteSubject.h"
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
					/// <summary> * ����۲��߶���ʵ�ָ��µķ�����ʹ�����״̬��Ŀ���״̬����һ�� </summary>
					/// 
					class ConcreteObserver : public Observer
					{
					///	
					///	 <summary> * ʾ�⣬�����ߵ�״̬ </summary>
					///	 
					private:
						std::string observerState;

					public:
						virtual void update(Subject *subject);
					};


				}
			}
		}
	}
}