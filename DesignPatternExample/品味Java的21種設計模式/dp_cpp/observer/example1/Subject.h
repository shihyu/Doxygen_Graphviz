#pragma once

#include "Observer.h"
#include <vector>

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
					/// <summary> * Ŀ�������֪���۲����Ĺ۲��ߣ����ṩע���ɾ���۲��ߵĽӿ� </summary>
					/// 
					class Subject
					{
					///	
					///	 <summary> * ��������ע��Ĺ۲��߶��� </summary>
					///	 
					private:
						std::vector<Observer*> observers;
					///	
					///	 <summary> * ע��۲��߶��� </summary>
					///	 * <param name="observer"> �۲��߶��� </param>
					///	 
					public:
						virtual void attach(Observer *observer);
					///	
					///	 <summary> * ɾ���۲��߶��� </summary>
					///	 * <param name="observer"> �۲��߶��� </param>
					///	 
						virtual void detach(Observer *observer);
					///	
					///	 <summary> * ֪ͨ����ע��Ĺ۲��߶��� </summary>
					///	 
					protected:
						virtual void notifyObservers();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								observers = std::vector<Observer*>();

								initialized = true;
							}
						}

public:
	Subject()
	{
		InitializeInstanceFields();
	}
					};


				}
			}
		}
	}
}