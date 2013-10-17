#pragma once

#include "Memento.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example2
				{

					///
					/// <summary> * ԭ�������� </summary>
					/// 
					class Originator
					{
					///	
					///	 <summary> * ʾ�⣬��ʾԭ������״̬ </summary>
					///	 
					private:
						std::string state;
					///	
					///	 <summary> * ��������ԭ���������״̬�ı���¼���� </summary>
					///	 * <returns> �����õı���¼���� </returns>
					///	 
					public:
						virtual Memento *createMemento();
					///	
					///	 <summary> * ��������ԭ���������״̬������ص�����¼�����¼��״̬ </summary>
					///	 * <param name="memento"> ��¼��ԭ����״̬�ı���¼���� </param>
					///	 
						virtual void setMemento(Memento *memento);
					///	
					///	 <summary> * �����ı���¼����ʵ�ֱ���¼խ�ӿ�
					///	 * ʵ�ֳ�˽�е��ڲ��࣬�����ⲿ���� </summary>
					///	 
					private:
						class MementoImpl : public Memento
						{
					///		
					///		 <summary> * ʾ�⣬��ʾ��Ҫ�����״̬ </summary>
					///		 
						private:
							std::string state;
						public:
							MementoImpl(std::string state);
							virtual std::string getState();

						private:
							bool initialized;
							void InitializeInstanceFields()
							{
								if ( ! initialized)
								{
									state = "";

									initialized = true;
								}
							}
						};


					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								state = "";

								initialized = true;
							}
						}

public:
	Originator()
	{
		InitializeInstanceFields();
	}
					};


				}
			}
		}
	}
}