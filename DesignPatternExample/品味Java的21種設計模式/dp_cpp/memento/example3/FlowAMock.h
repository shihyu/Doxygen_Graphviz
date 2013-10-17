#pragma once

#include "FlowAMockMemento.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example3
				{


					///
					/// <summary> * ģ����������A��ֻ��һ��ʾ�⣬��ָĳ���������� </summary>
					/// 
					class FlowAMock : public Serializable
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
					///	
					///	 <summary> * ��������ԭ���������״̬�ı���¼���� </summary>
					///	 * <returns> �����õı���¼���� </returns>
					///	 
						virtual FlowAMockMemento *createMemento();
					///	
					///	 <summary> * ��������ԭ���������״̬������ص�����¼�����¼��״̬ </summary>
					///	 * <param name="memento"> ��¼��ԭ����״̬�ı���¼���� </param>
					///	 
						virtual void setMemento(FlowAMockMemento *memento);
					///	
					///	 <summary> * �����ı���¼����ʵ�ֱ���¼խ�ӿ�
					///	 * ʵ�ֳ�˽�е��ڲ��࣬�����ⲿ���� </summary>
					///	 
					private:
						class MementoImpl : public FlowAMockMemento
						{
					///		
					///		 <summary> * ʾ�⣬����ĳ���м��� </summary>
					///		 
						private:
							int tempResult;
					///		
					///		 <summary> * ʾ�⣬����ĳ���м��� </summary>
					///		 
							std::string tempState;
						public:
							MementoImpl(int tempResult, std::string tempState);
							virtual int getTempResult();
							virtual std::string getTempState();
						};
					};

				}
			}
		}
	}
}