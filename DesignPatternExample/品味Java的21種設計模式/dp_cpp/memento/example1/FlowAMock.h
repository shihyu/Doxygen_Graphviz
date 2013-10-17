#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example1
				{
					///
					/// <summary> * ģ����������A��ֻ��һ��ʾ�⣬��ָĳ���������� </summary>
					/// 
					class FlowAMock
					{
					///	
					///	 <summary> * �������ƣ�����Ҫ�ⲿ�洢��״̬���� </summary>
					///	 
					private:
						std::string flowName;
					///	
					///	 <summary> * ʾ�⣬��ָĳ���м�������Ҫ�ⲿ�洢��״̬���� </summary>
					///	 
						int tempResult;
					///	
					///	 <summary> * ʾ�⣬��ָĳ���м�������Ҫ�ⲿ�洢��״̬���� </summary>
					///	 
						std::string tempState;
					///	
					///	 <summary> * ���췽���������������� </summary>
					///	 * <param name="flowName"> �������� </param>
					///	 
					public:
						FlowAMock(std::string flowName);

						virtual std::string getTempState();
						virtual void setTempState(std::string tempState);
						virtual int getTempResult();
						virtual void setTempResult(int tempResult);

					///	
					///	 <summary> * ʾ�⣬�������̵ĵ�һ���׶� </summary>
					///	 
						virtual void runPhaseOne();
					///	
					///	 <summary> * ʾ�⣬���շ���һ���������̺�벿�� </summary>
					///	 
						virtual void schema1();
					///	
					///	 <summary> * ʾ�⣬���շ��������������̺�벿�� </summary>
					///	 
						virtual void schema2();
					};

				}
			}
		}
	}
}