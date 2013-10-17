#pragma once

#include "Mediator.h"
#include "ConcreteColleagueA.h"
#include "ConcreteColleagueB.h"
#include "Colleague.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example1
				{
					///
					/// <summary> * ������н���ʵ�� </summary>
					/// 
					class ConcreteMediator : public Mediator
					{

					///	
					///	 <summary> * ���в�ά��ͬ��A </summary>
					///	 
					private:
						ConcreteColleagueA *colleagueA;
					///	
					///	 <summary> * ���в�ά��ͬ��B </summary>
					///	 
						ConcreteColleagueB *colleagueB;

					///	
					///	 <summary> * �����н�����Ҫ�˽Ⲣά����ͬ��A���� </summary>
					///	 * <param name="colleague"> ͬ��A���� </param>
					///	 
					public:
						virtual void setConcreteColleagueA(ConcreteColleagueA *colleague);
					///	
					///	 <summary> * �����н�����Ҫ�˽Ⲣά����ͬ��B���� </summary>
					///	 * <param name="colleague"> ͬ��B���� </param>
					///	 
						virtual void setConcreteColleagueB(ConcreteColleagueB *colleague);

						virtual void changed(Colleague *colleague);

					};

				}
			}
		}
	}
}